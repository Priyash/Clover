package com.clover.message.service;

import com.clover.message.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageQueueService {
    void send(Product product);
    List<Product> receive();
}
