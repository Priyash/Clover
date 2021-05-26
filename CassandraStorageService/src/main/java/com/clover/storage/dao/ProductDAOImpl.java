package com.clover.storage.dao;

import com.clover.storage.config.CassandraTemplateCustom;
import com.clover.storage.model.Product;
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
