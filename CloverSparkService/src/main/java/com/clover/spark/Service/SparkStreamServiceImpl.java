package com.clover.spark.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SparkStreamServiceImpl implements SparkStreamService{

    @Autowired
    private SparkStreamFromKafkaExecutorService sparkStreamFromKafkaExecutorService;

    public SparkStreamServiceImpl(){}

    @Override
    public void startStreamAndSaveToCassandra() {
        try {
            sparkStreamFromKafkaExecutorService.startSparkStreamFromKafkaAndSaveToCassandraAsyncTask();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
