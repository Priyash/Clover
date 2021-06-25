package com.clover.storage.repository;

import com.clover.storage.config.SparkConfigLoader;
import com.clover.storage.model.Product;
import com.clover.storage.service.CassandraStreamService;
import com.clover.storage.util.ElasticSearchUtil;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.querybuilder.BuiltStatement;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.AsyncCassandraTemplate;
import org.springframework.data.cassandra.core.EntityWriteResult;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.Duration;
import java.util.List;
import java.util.Map;


@Repository
public class CassandraAsyncTemplateCustom {

    @Autowired
    private AsyncCassandraTemplate asyncCassandraTemplate;

    @Autowired
    private CassandraStreamService cassandraStreamService;

    @Autowired
    private transient ElasticSearchUtil elasticSearchUtil;

    @Autowired
    private transient SparkConfigLoader sparkConfigLoader;

    public <T>  void createAsyncProduct(T entity) {
        ListenableFuture<EntityWriteResult<T>> insertResultFuture = asyncCassandraTemplate.insert(entity, InsertOptions
                                                                                            .builder()
                                                                                                .withIfNotExists()
                                                                                                .timeout(Duration.ofMillis(sparkConfigLoader
                                                                                                            .getConstants()
                                                                                                            .getTimeouts()
                                                                                                            .getCassandraInsertTimeout())).build());
        insertResultFuture.addCallback(new ListenableFutureCallback<EntityWriteResult<T>>() {
            @Override
            public void onSuccess(EntityWriteResult<T> entityWriteResult) {
                System.out.println("Write has been done successfully " + entityWriteResult.getRows().toString());
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("Exception while writing data to cassandra " + throwable.getStackTrace().toString());
            }
        });
    }

    @Deprecated
    public void fetchAsyncProducts() {
        BuiltStatement builtStatement = (BuiltStatement) QueryBuilder.select().all().from("product_keyspace", "product_table")
                                        .allowFiltering()
                                        .setFetchSize(1000)
                                        .setConsistencyLevel(ConsistencyLevel.ANY)
                                        .setReadTimeoutMillis(50000);

        ListenableFuture<List<Product>> selectFutureResults = asyncCassandraTemplate.select(builtStatement.getQueryString(new CodecRegistry()), Product.class);
        selectFutureResults.addCallback(new ListenableFutureCallback<List<Product>>() {
            @SneakyThrows
            @Override
            public void onSuccess(List<Product> products) {
                System.out.println("Successfully fetched the products of size : " + products.size());
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("Exception while querying data from cassandra " + throwable.getMessage());
            }
        });
    }
}
