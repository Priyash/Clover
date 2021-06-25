package com.clover.spark.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SparkStreamServiceImpl implements SparkStreamService{

    @Autowired
    private SparkStreamFromKafkaExecutorService kafkaExecutorService;

    public SparkStreamServiceImpl(){}

    @Override
    public void startStreamAndSaveToCassandra() {
        try {
            kafkaExecutorService.startSparkStreamFromKafkaAndSaveToCassandraAsyncTask();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
