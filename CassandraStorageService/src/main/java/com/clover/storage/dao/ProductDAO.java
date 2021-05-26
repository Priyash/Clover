package com.clover.storage.dao;

import com.clover.storage.model.Product;

public interface ProductDAO {
    void createProduct(Product product);
    Product getProduct(String id);
}
