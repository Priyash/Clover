package com.clover.store.spark.CloverSparkService.Service;

import com.clover.store.spark.CloverSparkService.Model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

@Service
public class SparkStreamExecutorService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private transient Gson gson;

    @Autowired
    private transient JavaSparkContext javaSparkContext;

    private transient JavaStreamingContext javaStreamingContext;

    @Value("${spark.stream.kafka.durations}")
    private String streamDurationTime;

    @Value("${kafka.broker.list}")
    private String metadataBrokerList;

    @Value("${spark.kafka.topics}")
    private String topicsAll;

    public SparkStreamExecutorService(){}


    @Async("asyncTaskExecutor")
    public void startSparkStreamAsyncTask(){

        try {
            Set<String> topicSet = new HashSet<>(Arrays.asList(topicsAll.split(",")));

            Map<String, String> kafkaParams = new HashMap<>();
            kafkaParams.put("metadata.broker.list", this.metadataBrokerList);

            javaStreamingContext = new JavaStreamingContext(javaSparkContext, Durations.seconds(Integer.valueOf(this.streamDurationTime)));
            javaStreamingContext.checkpoint("checkpoint");

            final JavaPairInputDStream<String, String> stream = KafkaUtils.createDirectStream(javaStreamingContext,
                    String.class, String.class,
                    StringDecoder.class, StringDecoder.class,
                    kafkaParams, topicSet);
            System.out.println("Stream started!");
            stream.foreachRDD(s -> {
                List<String> topicDataSet = s.values().collect();
                for (String topicData : topicDataSet) {
                    Product product = gson.fromJson(topicData, Product.class);

                }
            });
            javaStreamingContext.start();
            javaStreamingContext.awaitTermination();

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
