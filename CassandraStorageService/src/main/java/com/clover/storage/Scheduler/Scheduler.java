package com.clover.storage.Scheduler;

import com.clover.storage.config.SparkConfigLoader;
import com.clover.storage.model.Product;
import com.clover.storage.util.ElasticSearchUtil;
import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.datastax.spark.connector.japi.CassandraRow;
import com.datastax.spark.connector.japi.rdd.CassandraJavaRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
public class Scheduler implements Serializable {
    @Autowired
    private transient SparkConfigLoader sparkConfigLoader;

    @Autowired
    private transient JavaSparkContext javaSparkContext;

    @Autowired
    private transient ElasticSearchUtil elasticSearchUtil;

    @Scheduled(cron = "0 0/2 * 1/1 * ?")
    public void startCassandraStreamToElasticsearch() throws InterruptedException, ExecutionException, IOException {
        System.out.println("Starting cron job for every minute");

        CassandraJavaRDD<CassandraRow> cassandraJavaRDD = CassandraJavaUtil
                .javaFunctions(javaSparkContext)
                .cassandraTable(sparkConfigLoader.getCassandra().getKeyspace(),
                        sparkConfigLoader.getCassandra().getTable());
        JavaRDD<Product> productJavaRDD = cassandraJavaRDD.map(new Function<CassandraRow, Product>() {
            @Override
            public Product call(CassandraRow cassandraRow) throws Exception {
                Product javaRDDProduct = new Product();
                javaRDDProduct.setA(cassandraRow.getInt("a"));
                javaRDDProduct.setB(cassandraRow.getString("b"));
                return javaRDDProduct;
            }
        });

        List<Product> products = productJavaRDD.collect();
        if(!ObjectUtils.isEmpty(products)) {
            Map<String, Object> elasticSearchSaveResults = elasticSearchUtil.saveToElasticSearch(products);
            System.out.println("Cassandra Stream Insert Result Status : " + elasticSearchSaveResults.get(sparkConfigLoader.getStatusCodeName()));
        }
    }
}
