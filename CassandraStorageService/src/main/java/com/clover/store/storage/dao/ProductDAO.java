package com.clover.store.storage.dao;

import com.clover.store.storage.model.Product;

public interface ProductDAO {
    void createProduct(Product product);
    Product getProduct(String id);
}
