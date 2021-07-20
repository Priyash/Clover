package com.clover.data.controller;

import com.clover.data.model.Product;
import com.clover.data.model.ProductImage;
import com.clover.data.model.ProductOption;
import com.clover.data.model.ProductVariant;
import com.clover.data.service.MongoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return new ResponseEntity<>(productResult, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/products/{product_id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> updateProduct( @NonNull @PathVariable Long product_id, @RequestBody Map<String, Object> requestMap) {
        Product productResult = mongoService.updateProductEntry(product_id, requestMap);
        return new ResponseEntity<>(productResult, HttpStatus.OK);
    }




    @RequestMapping(value = "/products/{product_id}/options", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductOption>> createOption(@NonNull @PathVariable Long product_id, @RequestBody @NonNull Map<String, Object> requestMap) {
        requestMap.put("product_id", product_id);
        List<ProductOption> options = mongoService.createProductOptionEntry(requestMap);
        return new ResponseEntity<>(options, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/products/{product_id}/options", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductOption>> updateOptions(@NonNull @PathVariable Long product_id, @RequestBody Map<String, Object> requestMap) {
        requestMap.put("product_id", product_id);
        List<ProductOption> updatedOptions = mongoService.updateProductOptionEntry(product_id, requestMap);
        return new ResponseEntity<>(updatedOptions, HttpStatus.OK);
    }


    @RequestMapping(value = "/products/{product_id}/variants", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductVariant>> createVariant(@NonNull @PathVariable Long product_id, @RequestBody @NonNull Map<String, Object> requestMap) {
        requestMap.put("product_id", product_id);
        List<ProductVariant> variants = mongoService.createVariantEntry(requestMap);
        return new ResponseEntity<>(variants, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/products/{product_id}/variants", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductVariant>> updateVariants( @NonNull @PathVariable Long product_id, @RequestBody Map<String, Object> requestMap) {
        requestMap.put("product_id", product_id);
        List<ProductVariant> updatedVariants = mongoService.updateVariantEntry(requestMap);
        return new ResponseEntity<>(updatedVariants, HttpStatus.OK);
    }





    @RequestMapping(value = "/products/{product_id}/images", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductImage>> createImage(@NonNull @PathVariable Long product_id, @RequestBody @NonNull Map<String, Object> requestMap) {
        requestMap.put("product_id", product_id);
        List<ProductImage> images = mongoService.createImageEntry(requestMap);
        return new ResponseEntity<>(images, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/products/{product_id}/images", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductImage>> updateImages( @NonNull @PathVariable Long product_id, @RequestBody Map<String, Object> requestMap) {
        requestMap.put("product_id", product_id);
        List<ProductImage> updatedImages = mongoService.updateImageEntry(requestMap);
        return new ResponseEntity<>(updatedImages, HttpStatus.OK);
    }
}
