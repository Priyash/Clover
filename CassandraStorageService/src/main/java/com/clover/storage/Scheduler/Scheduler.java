package com.clover.storage.Scheduler;

import com.clover.storage.config.SparkConfigLoader;
import com.clover.storage.model.*;
import com.clover.storage.util.ElasticSearchUtil;
import com.datastax.spark.connector.japi.UDTValue;
import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.datastax.spark.connector.japi.CassandraRow;
import com.datastax.spark.connector.japi.rdd.CassandraJavaRDD;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import com.clover.storage.model.ProductRowReader.ProductRowReaderFactory;
import scala.Int;
import scala.Tuple2;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Component
@Slf4j
public class Scheduler implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private transient SparkConfigLoader sparkConfigLoader;

    @Autowired
    private transient JavaSparkContext javaSparkContext;

    @Autowired
    private transient ElasticSearchUtil elasticSearchUtil;

    @Autowired
    private transient ProductRowReaderFactory readerFactory;

    @Scheduled(cron = "0 0/2 * 1/1 * ?")
    public void startCassandraStreamToElasticsearch() throws InterruptedException, ExecutionException, IOException {
        log.info("Cron job started...");
        log.info("Cron expression: '0 0/2 * 1/1 * ?'");
        try {
            CassandraJavaRDD<Product> productJavaRDD = CassandraJavaUtil
                    .javaFunctions(javaSparkContext)
                    .cassandraTable(sparkConfigLoader.getCassandra().getKeyspace(),
                                    sparkConfigLoader.getCassandra().getTable(),
                                    readerFactory);

            //Collecting the list of product object from the spark stream.
            List<Product> products = productJavaRDD.collect();
            log.info("[Scheduler]Spark streaming context product size: {}", products.size());
            if (!ObjectUtils.isEmpty(products)) {
                //Sending the list of product objects to elastic search.
                Map<String, Object> elasticSearchSaveResults = elasticSearchUtil.saveToElasticSearch(products);
                log.info("[Scheduler]Elasticsearch save result status: {}", elasticSearchSaveResults.get(sparkConfigLoader.getStatusCodeName()));
            }
        } catch (Exception ex) {
            log.error("[Scheduler]Exception while starting scheduler for reading cassandra table ", ex);
        }
    }
}
