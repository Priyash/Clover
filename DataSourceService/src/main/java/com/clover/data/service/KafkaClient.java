package com.clover.data.service;

import com.clover.data.model.Product;

public interface KafkaClient {
    public String send(Product product);
}
