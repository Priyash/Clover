package com.clover.message.consumer;

import com.clover.message.config.AppConfig;
import com.clover.message.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Component
@Slf4j
public class CloverMessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(CloverMessageReceiver.class);
    private List<Product> productList = new CopyOnWriteArrayList<>();

    @Autowired
    private AppConfig appConfig;

    public CloverMessageReceiver(){}

    @KafkaListener(topics = "#{appConfig.getTopics().get(0)}",
                   groupId = "#{appConfig.getConsumer().getGroupID()}",
                   containerFactory = "ProductKafkaListenerContainerFactory")
    public void listener(Product product) {
        log.info("Listener : [{}]", String.valueOf(product));
        productList.add(product);
    }

    public List<Product> receive(){
        return productList;
    }
}
