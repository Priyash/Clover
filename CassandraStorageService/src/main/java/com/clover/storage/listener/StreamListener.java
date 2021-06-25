package com.clover.storage.listener;

import com.clover.storage.config.SparkConfigLoader;
import com.clover.storage.model.Product;
import com.clover.storage.service.CassandraStreamService;
import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.datastax.spark.connector.japi.CassandraRow;
import com.datastax.spark.connector.japi.rdd.CassandraJavaRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StreamListener {

    @Autowired
    private CassandraStreamService streamService;



    @EventListener
    public void streamOnApplicationStartup(final ApplicationReadyEvent event) throws InterruptedException {
        //streamService.startSparkStreamFromCassandraToElasticsearch();


    }
}
