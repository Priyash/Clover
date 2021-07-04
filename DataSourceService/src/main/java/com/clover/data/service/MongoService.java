package com.clover.data.service;

import com.clover.data.model.Product;

import java.util.Map;

public interface MongoService {
    Product createProductEntry(Map<String, Object> objectMap);
    Product updateProductEntry(Long id, Map<String, Object> objectMap);
}
