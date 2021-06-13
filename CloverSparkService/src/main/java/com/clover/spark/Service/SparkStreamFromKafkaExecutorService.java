package com.clover.spark.Service;

import com.clover.spark.Config.SparkConfigLoader;
import com.clover.spark.Model.Product;
import com.clover.spark.util.CassandraUtil;
import com.google.gson.Gson;
import kafka.serializer.StringDecoder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.StreamingContext;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CompletionException;

@Service
public class SparkStreamFromKafkaExecutorService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private transient Gson gson;

    @Autowired
    private transient JavaSparkContext javaSparkContext;

    private transient JavaStreamingContext javaStreamingContext;

    @Autowired
    private transient SparkConfigLoader sparkConfigLoader;

    @Autowired
    private transient CassandraUtil cassandraUtil;

    public SparkStreamFromKafkaExecutorService(){}

    @Async("asyncTaskExecutor")
    public void startSparkStreamFromKafkaAndSaveToCassandraAsyncTask(){
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
            //JavaPairInputDStream capture streams from kafkaUtils in chunks / batches , each chunks represent in form of RDD
            //conversion from RDD to List can be done using collect() method
            stream.foreachRDD(s -> {
                List<String> topicDataSet = s.values().collect();
                for (String topicData : topicDataSet) {
                    Product product = gson.fromJson(topicData, Product.class);
                    Map<String, Object> cassandraSaveResultMap = cassandraUtil.saveToCassandra(product);
                    System.out.println("Cassandra Stream Insert Result Status : " + cassandraSaveResultMap.get(sparkConfigLoader.getHttpEntityName()));
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
