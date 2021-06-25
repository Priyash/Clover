package com.clover.storage.service;

import com.clover.storage.config.SparkConfigLoader;
import com.clover.storage.model.Product;
import com.clover.storage.util.ElasticSearchUtil;
import com.datastax.spark.connector.japi.CassandraRow;
import com.datastax.spark.connector.japi.rdd.CassandraJavaRDD;
import com.google.gson.Gson;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.StreamingContextState;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
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

    @Async("asyncTaskExecutor")
    public void startSparkStreamFromCassandraToElasticsearch() throws InterruptedException {
        try {
            javaStreamingContext = new JavaStreamingContext(JavaSparkContext
                                                                .fromSparkContext(SparkContext
                                                                .getOrCreate(javaSparkContext
                                                                                .getConf())),
                                                                                Durations.seconds(Integer.valueOf(sparkConfigLoader.getKafka().getStream().getDurations())));

            javaStreamingContext.checkpoint(sparkConfigLoader.getCheckpoint());
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

            //Getting the casandra table products data as list
            List<Product> products = productJavaRDD.collect();

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
                //http://localhost:8092/api/v1/index
                if(!ObjectUtils.isEmpty(productList)) {
                    Map<String, Object> elasticSearchSaveResults = elasticSearchUtil.saveToElasticSearch(productList);
                    System.out.println("Cassandra Stream Insert Result Status : " + elasticSearchSaveResults.get(sparkConfigLoader.getStatusCodeName()));
                }
            });

            javaStreamingContext.start();
            javaStreamingContext.awaitTerminationOrTimeout(15000);
        } catch (CompletionException cex) {
            cex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
