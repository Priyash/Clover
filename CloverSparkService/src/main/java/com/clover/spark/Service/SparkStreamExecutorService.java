package com.clover.spark.Service;

import com.clover.spark.Config.SparkConfigLoader;
import com.clover.spark.Constants.Constants;
import com.clover.spark.Model.Product;
import com.clover.spark.client.CloverHttpClient;
import com.google.gson.Gson;
import kafka.serializer.StringDecoder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class SparkStreamExecutorService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private transient Gson gson;

    @Autowired
    private transient JavaSparkContext javaSparkContext;

    private transient JavaStreamingContext javaStreamingContext;

    @Autowired
    private SparkConfigLoader sparkConfigLoader;

    public SparkStreamExecutorService(){}

    @Async("asyncTaskExecutor")
    public void startSparkStreamAsyncTask(){
        try {
            Set<String> topicSet = new HashSet<>(Arrays.asList(sparkConfigLoader.getKafka().getTopics().split(",")));

            Map<String, String> kafkaParams = new HashMap<>();
            kafkaParams.put(sparkConfigLoader.getKafka().getBroker().getName(), sparkConfigLoader.getKafka().getBroker().getList());

            javaStreamingContext = new JavaStreamingContext(javaSparkContext, Durations.seconds(Integer.valueOf(sparkConfigLoader.getKafka().getStream().getDurations())));
            javaStreamingContext.checkpoint(sparkConfigLoader.getCheckpoint());

            final JavaPairInputDStream<String, String> stream = KafkaUtils.createDirectStream(javaStreamingContext,
                    String.class, String.class,
                    StringDecoder.class, StringDecoder.class,
                    kafkaParams, topicSet);
            System.out.println("Stream started!");
            stream.foreachRDD(s -> {
                List<String> topicDataSet = s.values().collect();
                for (String topicData : topicDataSet) {
                    Product product = gson.fromJson(topicData, Product.class);
//                    CompletableFuture<String> cassandraInsertFuture = cloverHttpClient.postHttpClient(Constants.CASSANDRA_STREAM_INSERT_URI);
//                    boolean completedOrTimeout = false;
//                    while(!completedOrTimeout) {
//                        if(cassandraInsertFuture.isDone() && !cassandraInsertFuture.isCancelled()){
//                            completedOrTimeout = true;
//                        }
//                    }
//                    String cassandra_save_result_status = cassandraInsertFuture.get();
                }
            });
            javaStreamingContext.start();
            javaStreamingContext.awaitTermination();
        } catch (CompletionException cex) {
            cex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void destroy(){
        if(!ObjectUtils.isEmpty(javaStreamingContext)){
            javaStreamingContext.stop();
            javaStreamingContext.close();
        }
    }

}
