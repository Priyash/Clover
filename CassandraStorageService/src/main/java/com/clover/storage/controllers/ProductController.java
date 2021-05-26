package com.clover.storage.controllers;

import com.clover.storage.dao.ProductDAO;
import com.clover.storage.model.Product;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    private ProductDAO productDAO;

    //http://localhost:8080/api/v1/product/create
    @RequestMapping(value = "/api/v1/product/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        if(!ObjectUtils.isEmpty(product)){
            productDAO.createProduct(product);
        }
        return new ResponseEntity<Product>(product, HttpStatus.CREATED);
    }


}
