package com.clover.store.storage.service;

import com.clover.store.storage.model.Product;

public interface StorageService {
    void createProduct(Product product);
    Product fetchProduct(String id);
}
