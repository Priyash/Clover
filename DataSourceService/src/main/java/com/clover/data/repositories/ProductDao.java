package com.clover.data.repositories;

import com.clover.data.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class ProductDao {

    @Autowired
    private MongoOperations mongoTemplate;

    public <T> T save(T entity) {
        T result = mongoTemplate.insert(entity);
        return result;
    }

    public <T> T update(T entity) {
        T result = mongoTemplate.save(entity);
        return result;
    }

    public <T> T find(T id) {
        Query query = new Query(Criteria.where("product_id").is(id));
        T productResult = (T) mongoTemplate.findOne(query, Product.class);
        return productResult;
    }

}
