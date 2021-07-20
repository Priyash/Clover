package com.clover.data.repositories;

import com.clover.data.model.Product;
import com.clover.data.utility.Constants;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
@Slf4j
public class ProductDaoImpl implements ProductDao{

    @Autowired
    private MongoTemplate mongoTemplate;

    public <T> T saveProduct(T entity) {
        T result = null;
        if(!ObjectUtils.isEmpty(entity)) {
            result = mongoTemplate.insert(entity);
        }
        return result;
    }

    public <T> T updateProduct(T entity) {
        T result = null;
        if(!ObjectUtils.isEmpty(entity)){
            result = mongoTemplate.save(entity);
        }
        return result;
    }

    public <T> Product fetchProduct(T id) {
        Query query = new Query(Criteria.where(Constants.ID).is(id));
        Product productResult = mongoTemplate.findOne(query, Product.class);
        log.info("Fetched product id: {}", productResult.getId());
        return productResult;
    }

    @Override
    public <T> List<Product> findActiveProducts() {
        Query activeProductQuery = new Query(Criteria.where(Constants.STATUS).is(Constants.ACTIVE));
        List<Product> products = mongoTemplate.find(activeProductQuery, Product.class);
        log.info("Fetched active products count: {}", products.size());
        return products;
    }

    @Override
    public <T> List<Product> updateProductStatus(List<Product> products) {
        if(!products.isEmpty()) {
            products.forEach(product -> {
                Query updateProductStatusQuery = new Query(Criteria.where(Constants.ID).is(product.getId()));
                UpdateResult result = mongoTemplate.upsert(updateProductStatusQuery, Update.update(Constants.STATUS, Constants.DRAFT), Product.class);
                log.info("Updated records acknowledged: {}", result.wasAcknowledged());
                log.info("Updated record id: {}", product.getId());
            });
        }
        return products;
    }
}
