package com.clover.store.clover.service;

import com.clover.store.clover.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageQueueService {
    void send(Product product);
    List<Product> receive();
}
