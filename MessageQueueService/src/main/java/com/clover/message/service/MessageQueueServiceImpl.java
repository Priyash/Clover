package com.clover.message.service;


import com.clover.message.config.AppConfig;
import com.clover.message.producer.CloverMessageSender;
import com.clover.message.model.Product;
import com.clover.message.consumer.CloverMessageReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageQueueServiceImpl implements MessageQueueService{
    private static Logger LOG = LoggerFactory.getLogger(MessageQueueServiceImpl.class);

    @Autowired
    private CloverMessageSender cloverMessageSender;

    @Autowired
    private CloverMessageReceiver cloverMessageReceiver;

    @Autowired
    private AppConfig appConfig;

    @Override
    public void send(Product product) {
        try {
            cloverMessageSender.sendMessageWithCallback(product, appConfig.getTopics().get(0));
        } catch (Exception ex) {
            LOG.error("Exception on sending message : {} ", product, ex);
        }
    }

    @Override
    public List<Product> receive() {
        try {
            List<Product> productList = cloverMessageReceiver.receive();
            return productList;
        } catch (Exception ex) {
            LOG.error("Exception on receiving message : {} ", ex);
        }
        return null;
    }
}
