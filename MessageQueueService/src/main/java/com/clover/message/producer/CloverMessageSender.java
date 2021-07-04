package com.clover.message.producer;

import com.clover.message.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class CloverMessageSender {
    private static KafkaTemplate<String, Product> kafkaTemplate;

    @Autowired
    public CloverMessageSender(KafkaTemplate<String, Product> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Product product, String topicName) {
        log.info("Sending : {}", String.valueOf(product));
        log.info("--------------------------------");

        kafkaTemplate.send(topicName, product);
    }

    public void sendMessageWithCallback(Product product, String topicName) {
        log.info("Sending : {}", String.valueOf(product));
        log.info("---------------------------------");

        ListenableFuture<SendResult<String, Product>> future = kafkaTemplate.send(topicName, product);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Product>>() {
            @Override
            public void onSuccess(SendResult<String, Product> result) {
                log.info("Success Callback: [{}] delivered with offset -{}", String.valueOf(product),
                        result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.warn("Failure Callback: Unable to deliver message [{}]. {}", String.valueOf(product), ex.getMessage());
            }
        });
    }
}
