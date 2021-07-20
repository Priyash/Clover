package com.clover.storage.service;

import com.clover.storage.config.SparkConfigLoader;
import com.clover.storage.model.*;
import com.clover.storage.util.ElasticSearchUtil;
import com.datastax.spark.connector.japi.CassandraRow;
import com.datastax.spark.connector.japi.rdd.CassandraJavaRDD;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import com.datastax.spark.connector.japi.CassandraJavaUtil;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CompletionException;

@Service
@Slf4j
public class CassandraStreamService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private transient Gson gson;

    @Autowired
    private transient JavaSparkContext javaSparkContext;

    private transient JavaStreamingContext javaStreamingContext;

    @Autowired
    private transient SparkConfigLoader sparkConfigLoader;

    @Autowired
    private transient ElasticSearchUtil elasticSearchUtil;

    @Autowired
    private transient ProductRowReader.ProductRowReaderFactory readerFactory;

    @Async("asyncTaskExecutor")
    public void startSparkStreamFromCassandraToElasticsearch() throws InterruptedException {
        try {
            javaStreamingContext = new JavaStreamingContext(JavaSparkContext
                                        .fromSparkContext(SparkContext
                                        .getOrCreate(javaSparkContext
                                                        .getConf())),
                                                        Durations.seconds(Integer.valueOf(sparkConfigLoader
                                                                                        .getConstants()
                                                                                        .getTimeouts()
                                                                                        .getSparkStreamingContextTimeout())));

            javaStreamingContext.checkpoint(sparkConfigLoader.getCheckpoint());
            CassandraJavaRDD<Product> cassandraJavaRDD = CassandraJavaUtil
                                                        .javaFunctions(javaSparkContext)
                                                        .cassandraTable(sparkConfigLoader.getCassandra().getKeyspace(),
                                                                        sparkConfigLoader.getCassandra().getTable(),
                                                                        readerFactory);

            //Getting the casandra table products data as list
            List<Product> products = cassandraJavaRDD.collect();
            log.info("[CassandraStreamService]Spark streaming context product size: {}", products.size());
            //Converting list to JavaRDD
            JavaRDD<Product> productRDD = javaStreamingContext.sparkContext().parallelize(products);

            //Converting JavaRDD to small batches in queue
            Queue<JavaRDD<Product>> productQueue = new LinkedList<>();
            productQueue.add(productRDD);

            //Converting small batches in queue to stream.
            JavaDStream<Product> productDStream = javaStreamingContext.queueStream(productQueue);

            //Iterating the RDD and saving it in elasticsearch
            productDStream.foreachRDD(stream -> {
                List<Product> productList = stream.collect();
                if(!ObjectUtils.isEmpty(productList)) {
                    Map<String, Object> elasticSearchSaveResults = elasticSearchUtil.saveToElasticSearch(productList);
                    log.info("[CassandraStreamService]Elasticsearch save result status: {}", elasticSearchSaveResults.get(sparkConfigLoader.getStatusCodeName()));
                }
            });

            javaStreamingContext.start();
            javaStreamingContext.awaitTerminationOrTimeout(sparkConfigLoader
                                                            .getConstants()
                                                            .getTimeouts().getSparkStreamingContextTimeout());
            log.info("[CassandraStreamService]Spark streaming context started!!!");
        } catch (CompletionException cex) {
            log.error("[CassandraStreamService]Spark streaming context completion exception ", cex);
        } catch (Exception ex) {
            log.error("[CassandraStreamService]Spark streaming context exception ", ex);
        }
    }
}
