package com.clover.store.clover.producer;

import com.clover.store.clover.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class CloverMessageSender {
    private final Logger LOG = LoggerFactory.getLogger(CloverMessageSender.class);
    private static KafkaTemplate<String, Product> kafkaTemplate;

    @Autowired
    public CloverMessageSender(KafkaTemplate<String, Product> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Product product, String topicName) {
        LOG.info("Sending : {}", String.valueOf(product));
        LOG.info("--------------------------------");

        kafkaTemplate.send(topicName, product);
    }

    public void sendMessageWithCallback(Product product, String topicName) {
        LOG.info("Sending : {}", String.valueOf(product));
        LOG.info("---------------------------------");

        ListenableFuture<SendResult<String, Product>> future = kafkaTemplate.send(topicName, product);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Product>>() {
            @Override
            public void onSuccess(SendResult<String, Product> result) {
                LOG.info("Success Callback: [{}] delivered with offset -{}", String.valueOf(product),
                        result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                LOG.warn("Failure Callback: Unable to deliver message [{}]. {}", String.valueOf(product), ex.getMessage());
            }
        });
    }
}
