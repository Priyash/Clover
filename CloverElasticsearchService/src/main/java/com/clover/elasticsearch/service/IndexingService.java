package com.clover.elasticsearch.service;

import com.clover.elasticsearch.model.Product;

import java.util.List;

public interface IndexingService {

    public void bulkIndexProductData(List<Product> products);
}
