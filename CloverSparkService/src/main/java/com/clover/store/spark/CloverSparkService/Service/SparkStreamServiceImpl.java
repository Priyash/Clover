package com.clover.store.spark.CloverSparkService.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SparkStreamServiceImpl implements SparkStreamService{

    @Autowired
    private SparkStreamExecutorService sparkStreamExecutorService;

    public SparkStreamServiceImpl(){}

    @Override
    public void startStream() {
        try {
            sparkStreamExecutorService.startSparkStreamAsyncTask();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
