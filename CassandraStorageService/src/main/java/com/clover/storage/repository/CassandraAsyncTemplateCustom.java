package com.clover.storage.repository;

import com.clover.storage.config.SparkConfigLoader;
import com.clover.storage.model.Product;
import com.clover.storage.service.CassandraStreamService;
import com.clover.storage.util.Constants;
import com.clover.storage.util.ElasticSearchUtil;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.querybuilder.BuiltStatement;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.AsyncCassandraTemplate;
import org.springframework.data.cassandra.core.EntityWriteResult;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.data.cassandra.core.UpdateOptions;
import org.springframework.stereotype.Repository;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.Duration;
import java.util.List;


@Repository
@Slf4j
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
                                                                            .getCassandraInsertOrUpdateTimeout()))
                                                                .build());
        insertResultFuture.addCallback(new ListenableFutureCallback<EntityWriteResult<T>>() {
            @Override
            public void onSuccess(EntityWriteResult<T> entityWriteResult) {
                log.info("[createAsyncProduct]Write has been done successfully, entity: {}, executionInfo: {}, rows: {}, wasApplied: {}",
                                                entity,
                                                entityWriteResult.getExecutionInfo(),
                                                entityWriteResult.getRows(),
                                                entityWriteResult.wasApplied());
            }

            @Override
            public void onFailure(Throwable throwable) {
                log.error("[createAsyncProduct]Exception while writing data to cassandra, exception: {}", throwable.getStackTrace().toString());
            }
        });
    }

    public <T> void updateAsyncProduct(T entity) {
        ListenableFuture<EntityWriteResult<T>> updateResultFuture = asyncCassandraTemplate.update(entity, UpdateOptions
                                                                    .builder()
                                                                    .timeout(Duration.ofMillis(sparkConfigLoader
                                                                            .getConstants()
                                                                            .getTimeouts()
                                                                            .getCassandraInsertOrUpdateTimeout()))
                                                                    .withIfExists()
                                                                    .build());
        updateResultFuture.addCallback(new ListenableFutureCallback<EntityWriteResult<T>>() {
            @Override
            public void onSuccess(EntityWriteResult<T> entityWriteResult) {
                log.info("[updateAsyncProduct]Update has been done successfully, entity: {}, executionInfo: {}, rows: {}, wasApplied: {}",
                        entity,
                        entityWriteResult.getExecutionInfo(),
                        entityWriteResult.getRows(),
                        entityWriteResult.wasApplied());
            }

            @Override
            public void onFailure(Throwable throwable) {
                log.error("[updateAsyncProduct]Exception while updating data to cassandra, exception: {} ", throwable.getStackTrace().toString());
            }
        });

    }

    @Deprecated
    public void fetchAsyncProducts() {
        BuiltStatement builtStatement = (BuiltStatement) QueryBuilder.select().all().from(Constants.CASSANDRA_KEYSPACE, Constants.CASSANDRA_TBL)
                                        .allowFiltering()
                                        .setFetchSize(1000)
                                        .setConsistencyLevel(ConsistencyLevel.ANY)
                                        .setReadTimeoutMillis(50000);

        ListenableFuture<List<Product>> selectFutureResults = asyncCassandraTemplate.select(builtStatement.getQueryString(new CodecRegistry()), Product.class);
        selectFutureResults.addCallback(new ListenableFutureCallback<List<Product>>() {
            @SneakyThrows
            @Override
            public void onSuccess(List<Product> products) {
                log.info("[fetchAsyncProducts]Successfully fetched the products of size: {}", products.size());
            }

            @Override
            public void onFailure(Throwable throwable) {
                log.error("[fetchAsyncProducts]Exception while fetching data from cassandra, exception: {} ", throwable.getStackTrace().toString());
            }
        });
    }
}
