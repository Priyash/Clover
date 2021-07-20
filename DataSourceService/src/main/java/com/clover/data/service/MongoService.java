package com.clover.data.service;

import com.clover.data.model.Product;
import com.clover.data.model.ProductImage;
import com.clover.data.model.ProductOption;
import com.clover.data.model.ProductVariant;

import java.util.List;
import java.util.Map;

public interface MongoService {
    Product createProductEntry(Map<String, Object> objectMap);
    Product updateProductEntry(Long id, Map<String, Object> objectMap);

    List<ProductOption> createProductOptionEntry(Map<String, Object> objectMap);
    List<ProductOption> updateProductOptionEntry(Long id, Map<String, Object> objectMap);

    List<ProductVariant> createVariantEntry(Map<String, Object> objectMap);
    List<ProductVariant> updateVariantEntry(Map<String, Object> objectMap);

    List<ProductImage> createImageEntry(Map<String, Object> objectMap);
    List<ProductImage> updateImageEntry(Map<String, Object> objectMap);

    List<Product> fetchActiveProducts();

}
