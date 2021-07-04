package com.clover.data.service;

import com.clover.data.builder.OptionsBuilder;
import com.clover.data.builder.ProductBuilder;
import com.clover.data.model.Product;
import com.clover.data.repositories.ProductDao;
import com.clover.data.utility.Generator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class MongoServiceImpl implements MongoService{

    @Autowired
    private ProductDao dao;

    @Autowired
    @Qualifier("sequenceGenerator")
    private Generator generator;

    @Autowired
    private ProductBuilder productBuilder;

    @Autowired
    private ProductDao productDao;

    @Override
    public Product createProductEntry(Map<String, Object> objectMap) {
        try {
            Long product_id = (Long) generator.generate();
            objectMap.putIfAbsent("product_id", product_id);
            Product createdProduct = productBuilder.build(objectMap);
            productDao.save(createdProduct);
            return createdProduct;
        } catch (Exception ex) {
            log.error("Exception while generating the product from MongoService ", ex);
        }
        return null;
    }

    @Override
    public Product updateProductEntry(Long product_id, Map<String, Object> objectMap) {
        try {
            objectMap.putIfAbsent("product_id", product_id);
            //Do a search with the product_id in mongo db collection
            //Then will get the ProductBuilder method to update.
        } catch (Exception ex) {
            log.error("Exception while generating the product from MongoService ", ex);
        }
        return null;
    }
}
