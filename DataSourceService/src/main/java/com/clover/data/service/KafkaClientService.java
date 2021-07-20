package com.clover.data.service;

import com.clover.data.model.Product;
import com.clover.data.utility.Constants;
import com.clover.data.utility.KafkaClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class KafkaClientService implements KafkaClient{
    @Autowired
    private KafkaClientUtil kafkaClientUtil;

    @Override
    public String send(Product product) {
        try {
            Map<String, Object> result = kafkaClientUtil.sendToKafkaStream(product);
            return result.get(Constants.STATUS_CODE).toString();
        } catch (Exception ex) {
            log.error("Exception while sending the product data to kafka stream , product id: {}", product.getId(), ex);
        }
        return null;
    }
}
