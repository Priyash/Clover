package com.clover.spark.Listener;

import com.clover.spark.Service.SparkStreamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SparkEventListener {
    @Autowired
    private SparkStreamService sparkStreamService;

    @EventListener
    public void onStartUp(final ApplicationReadyEvent event){
        System.out.println("Stream starting from onStartUp");
        log.info("Spark streaming started on Application startup..");
        sparkStreamService.startStreamAndSaveToCassandra();
    }
}
