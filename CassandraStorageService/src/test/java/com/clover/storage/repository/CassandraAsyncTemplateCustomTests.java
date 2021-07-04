package com.clover.storage.repository;

import com.clover.storage.config.AppConfig;
import com.clover.storage.config.CassandraConfig;
import com.clover.storage.config.CassandraConfigLoader;
import com.clover.storage.model.Product;
import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.querybuilder.BuiltStatement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.core.AsyncCassandraOperations;
import org.springframework.data.cassandra.core.EntityWriteResult;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.data.cassandra.core.AsyncCassandraTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.Duration;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {CassandraConfig.class, AppConfig.class})
public class CassandraAsyncTemplateCustomTests {

    @Autowired
    private AsyncCassandraTemplate asyncCassandraTemplate;

    @Test
    public void shouldFetchProductInfo_WhenDoingSelectCall() {
        BuiltStatement builtStatement = (BuiltStatement) QueryBuilder.select().all().from("product_keyspace", "product_table")
                .allowFiltering()
                .limit(1000)
                .setFetchSize(100)
                .setConsistencyLevel(ConsistencyLevel.ALL)
                .setReadTimeoutMillis(1000);

        ListenableFuture<List<Product>> selectFutureResults = asyncCassandraTemplate.select(builtStatement.getQueryString(new CodecRegistry()), Product.class);
        selectFutureResults.addCallback(new ListenableFutureCallback<List<Product>>() {
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("Exception while querying data from cassandra " + throwable.getMessage());
            }

            @Override
            public void onSuccess(List<Product> products) {
                System.out.println("Query from the cassandra table has been done successfully of size : " + products.size());
            }
        });
    }

    @Test
    public void createAsyncProduct() {
        Product testProduct = new Product();
        testProduct.setA(65);
        testProduct.setB("105.9 Cubic Foot Compact Cube Office Refrigerators");

        ListenableFuture<EntityWriteResult<Product>> insertResultFuture = asyncCassandraTemplate.insert(testProduct, InsertOptions.builder().timeout(Duration.ofMillis(1000)).build());
        insertResultFuture.addCallback(new ListenableFutureCallback<EntityWriteResult<Product>>() {
            @Override
            public void onSuccess(EntityWriteResult<Product> entityWriteResult) {
                System.out.println("Write has been done successfully " + entityWriteResult.getEntity().toString());
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("Exception while writing data to cassandra " + throwable.getMessage());
            }

        });
    }

}
