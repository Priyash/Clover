package com.clover.store.storage.dao;

import com.clover.store.storage.config.CassandraTemplateCustom;
import com.clover.store.storage.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDAOImpl implements ProductDAO{

    @Autowired
    private CassandraTemplateCustom cassandraTemplateCustom;

    @Override
    public void createProduct(Product product) {
        cassandraTemplateCustom.create(product);
    }

    @Override
    public Product getProduct(String id) {
        return cassandraTemplateCustom.findById(id, Product.class);
    }
}
