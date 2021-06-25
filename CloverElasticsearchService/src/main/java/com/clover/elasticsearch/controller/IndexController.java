package com.clover.elasticsearch.controller;

import com.clover.elasticsearch.model.Product;
import com.clover.elasticsearch.service.IndexingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class IndexController {

    @Autowired
    private IndexingService indexingService;

    //http://localhost:8092/api/v1/index
    @RequestMapping(value = "/index", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveIndexData(@RequestBody List<Product> products) {
        indexingService.bulkIndexProductData(products);
        return new ResponseEntity<String>("Started bulk Indexing", HttpStatus.CREATED);
    }
}
