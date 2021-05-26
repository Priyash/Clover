package com.clover.store.clover.consumer;

import com.clover.store.clover.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Component
public class CloverMessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(CloverMessageReceiver.class);
    private List<Product> productList = new CopyOnWriteArrayList<>();
    public CloverMessageReceiver(){}

    @KafkaListener(topics = "cloverMessage",
                   groupId = "clover-consumer-group-ID",
                   containerFactory = "ProductKafkaListenerContainerFactory")
    public void listener(Product product) {
        LOG.info("Listener : [{}]", String.valueOf(product));
        productList.add(product);
    }

    public List<Product> receive(){
        return productList;
    }
}
