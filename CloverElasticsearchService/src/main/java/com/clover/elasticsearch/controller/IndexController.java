package com.clover.elasticsearch.controller;

import com.clover.elasticsearch.model.Product;
import com.clover.elasticsearch.service.IndexingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
@Slf4j
public class IndexController {

    @Autowired
    private IndexingService indexingService;

    //http://localhost:8092/api/v1/index
    @RequestMapping(value = "/index", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveIndexData(@RequestBody List<Product> products) {
        log.info("IndexController of CloverElasticSearchService Request Body: {}, productsSize: {}", products.toArray().toString(), products.size());
        indexingService.bulkIndexProductData(products);
        return new ResponseEntity<String>("Started bulk Indexing", HttpStatus.OK);
    }
}
