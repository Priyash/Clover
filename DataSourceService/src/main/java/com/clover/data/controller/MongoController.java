package com.clover.data.controller;

import com.clover.data.model.Product;
import com.clover.data.service.MongoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1")
@Slf4j
public class MongoController {

    @Autowired
    private MongoService mongoService;

    @RequestMapping(value = "/products/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createProduct(@RequestBody @NonNull Map<String, Object> requestMap) {
        Product productResult = mongoService.createProductEntry(requestMap);
        return new ResponseEntity<Product>(productResult, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/products/{product_id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> updateProduct(@RequestBody @NonNull @PathVariable Long product_id, Map<String, Object> requestMap) {
        Product productResult = mongoService.updateProductEntry(product_id, requestMap);
        return new ResponseEntity<Product>(productResult, HttpStatus.OK);
    }
}
