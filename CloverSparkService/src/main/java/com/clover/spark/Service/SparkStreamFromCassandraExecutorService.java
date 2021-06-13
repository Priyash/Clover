package com.clover.spark.Service;

import com.clover.spark.Config.SparkConfigLoader;
import com.clover.spark.Model.Product;
import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.datastax.spark.connector.japi.CassandraRow;
import com.datastax.spark.connector.japi.rdd.CassandraJavaRDD;
import com.google.gson.Gson;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.elasticsearch.spark.streaming.api.java.JavaEsSparkStreaming;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
public class SparkStreamFromCassandraExecutorService implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private transient Gson gson;

    @Autowired
    private transient JavaSparkContext javaSparkContext;

    private transient JavaStreamingContext javaStreamingContext;

    @Autowired
    private transient SparkConfigLoader sparkConfigLoader;

    public SparkStreamFromCassandraExecutorService(){}

    @Async("asyncTaskExecutor")
    //reference_1 : https://stackoverflow.com/questions/56167569/how-to-copy-data-from-cassandra-to-elastic-search-6-7
    //reference_2 : https://docs.datastax.com/en/dse/5.1/dse-dev/datastax_enterprise/spark/sparkJavaApi.html
    public void startSparkStreamFromCassandraAndSaveToElasticsearchAsyncTask() {
        try {
            //Initiating and fetching the cassandra row and converting it to JavaRDD type from CassandraJavaRDD
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

            //Initiating the javaStreamingContext
            javaStreamingContext = new JavaStreamingContext(javaSparkContext, Durations.seconds(Integer.valueOf(sparkConfigLoader.getKafka().getStream().getDurations())));
            javaStreamingContext.checkpoint(sparkConfigLoader.getCheckpoint());

            //Converting the JavaRDD type to micro-batches using queue
            Queue<JavaRDD<Product>> microBatches = new LinkedList<>();
            microBatches.add(productJavaRDD);

            //Adding the queue to the javaStreamingContext queueStream
            JavaDStream<Product> productJavaDStream = javaStreamingContext.queueStream(microBatches);

            productJavaDStream.foreachRDD(s -> {
                List<Product> productRDDList = s.collect();
                productRDDList.forEach(product -> {
                    // call elastic search url using post call
                });

            });
            //Saving data to elastic search
            //JavaEsSparkStreaming.saveToEs(productJavaDStream, "elasticsearch_index/product");

            //start the java stream
            javaStreamingContext.start();
            javaStreamingContext.awaitTermination();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
