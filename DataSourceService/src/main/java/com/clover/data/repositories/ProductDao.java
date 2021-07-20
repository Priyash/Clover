package com.clover.data.repositories;

import com.clover.data.model.Product;

import java.util.List;

public interface ProductDao {
    public <T> T saveProduct(T entity);
    public <T> T updateProduct(T entity);
    public <T> Product fetchProduct(T id);

    public  <T> List<Product> findActiveProducts();
    public <T> List<Product> updateProductStatus(List<Product> products);
}
