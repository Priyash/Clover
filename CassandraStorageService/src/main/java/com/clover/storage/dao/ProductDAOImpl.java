package com.clover.storage.dao;

import com.clover.storage.repository.CassandraAsyncTemplateCustom;
import com.clover.storage.repository.CassandraTemplateCustom;
import com.clover.storage.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductDAOImpl implements ProductDAO{

    @Autowired
    private CassandraAsyncTemplateCustom cassandraAsyncTemplateCustom;

    @Override
    public void createProduct(Product product) {
        cassandraAsyncTemplateCustom.createAsyncProduct(product);
    }
}
