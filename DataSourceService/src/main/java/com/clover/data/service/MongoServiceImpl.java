package com.clover.data.service;

import com.clover.data.builder.ProductBuilder;
import com.clover.data.builder.ProductImageBuilder;
import com.clover.data.builder.ProductOptionBuilder;
import com.clover.data.builder.ProductVariantBuilder;
import com.clover.data.model.Product;
import com.clover.data.model.ProductImage;
import com.clover.data.model.ProductOption;
import com.clover.data.model.ProductVariant;
import com.clover.data.repositories.ProductDao;
import com.clover.data.utility.Generator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MongoServiceImpl implements MongoService{


    @Autowired
    @Qualifier("sequenceGenerator")
    private Generator generator;

    @Autowired
    private ProductBuilder productBuilder;

    @Autowired
    private ProductVariantBuilder variantBuilder;

    @Autowired
    private ProductImageBuilder imageBuilder;

    @Autowired
    private ProductOptionBuilder optionBuilder;

    @Autowired
    private ProductDao dao;

    @Override
    public Product createProductEntry(Map<String, Object> objectMap) {
        try {
            Long product_id = (Long) generator.generate();
            objectMap.putIfAbsent("product_id", product_id);
            Product createdProduct = productBuilder.build(objectMap);
            dao.saveProduct(createdProduct);
            return createdProduct;
        } catch (Exception ex) {
            log.error("[DAO]Exception while generating the product from MongoService ", ex);
        }
        return null;
    }

    @Override
    public Product updateProductEntry(Long product_id, Map<String, Object> objectMap) {
        try {
            objectMap.putIfAbsent("product_id", product_id);
            Product existingProduct = dao.fetchProduct(product_id);
            Product updatedProduct = productBuilder.updateObject(existingProduct, objectMap);
            dao.updateProduct(updatedProduct);
            return updatedProduct;
        } catch (Exception ex) {
            log.error("[DAO]Exception while generating the product from MongoService ", ex);
        }
        return null;
    }

    @Override
    public List<ProductOption> createProductOptionEntry(Map<String, Object> objectMap) {
        try {
            Long product_id = (Long) objectMap.get("product_id");
            Product existingProduct = dao.fetchProduct(product_id);
            List<ProductOption> existingProductOptions = existingProduct.getOptions();
            List<ProductOption> newProductOptions = optionBuilder.build(objectMap);
            existingProductOptions.addAll(newProductOptions);
            Product updatedProduct = existingProduct.toBuilder()
                    .options(existingProductOptions)
                    .build();
            dao.updateProduct(updatedProduct);
            return existingProductOptions;
        } catch (Exception ex) {
            log.error("[DAO]Exception while generating the product from MongoService ", ex);
        }
        return null;
    }

    @Override
    public List<ProductOption> updateProductOptionEntry(Long id, Map<String, Object> objectMap) {
        try {
            Long product_id = (Long) objectMap.get("product_id");
            Product existingProduct = dao.fetchProduct(product_id);
            List<ProductOption> newProductOptions = optionBuilder.updateObject(existingProduct, objectMap);
            Product updatedProduct = existingProduct.toBuilder()
                    .options(newProductOptions)
                    .build();
            dao.updateProduct(updatedProduct);
            return updatedProduct.getOptions();
        } catch (Exception ex) {
            log.error("[DAO]Exception while generating the product from MongoService ", ex);
        }
        return null;
    }

    @Override
    public List<ProductVariant> createVariantEntry(Map<String, Object> objectMap) {
        try {
            Long product_id = (Long) objectMap.get("product_id");
            Product existingProduct = dao.fetchProduct(product_id);
            List<ProductVariant> existingVariants = existingProduct.getVariants();
            List<ProductVariant> newVariants = variantBuilder.build(objectMap);
            existingVariants.addAll(newVariants);
            Product updatedProduct = existingProduct.toBuilder()
                                                    .variants(existingVariants)
                                                    .build();
            dao.updateProduct(updatedProduct);
            return existingVariants;
        } catch (Exception ex) {
            log.error("[DAO]Exception while generating the variant from MongoService ", ex);
        }
        return null;
    }

    @Override
    public List<ProductVariant> updateVariantEntry(Map<String, Object> objectMap) {
        try {
            Long product_id = (Long) objectMap.get("product_id");
            Product existingProduct = dao.fetchProduct(product_id);
            List<ProductVariant> newVariants = variantBuilder.updateObject(existingProduct, objectMap);
            Product updatedProduct = existingProduct.toBuilder()
                    .variants(newVariants)
                    .build();
            dao.updateProduct(updatedProduct);
            return updatedProduct.getVariants();
        } catch (Exception ex) {
            log.error("Exception while updating variant entry ", ex);
        }
        return null;
    }

    @Override
    public List<ProductImage> createImageEntry(Map<String, Object> objectMap) {
        try {
            Long product_id = (Long) objectMap.get("product_id");
            Product existingProduct = dao.fetchProduct(product_id);
            List<ProductImage> existingImages = existingProduct.getImages();
            List<ProductImage> newImages = imageBuilder.build(objectMap);
            existingImages.addAll(newImages);
            Product updatedProduct = existingProduct.toBuilder()
                    .images(existingImages)
                    .build();
            dao.updateProduct(updatedProduct);
            return existingImages;
        } catch (Exception ex) {
            log.error("Exception while creating image entry ", ex);
        }
        return null;
    }

    @Override
    public List<ProductImage> updateImageEntry(Map<String, Object> objectMap) {
        try {
            Long product_id = (Long) objectMap.get("product_id");
            Product existingProduct = dao.fetchProduct(product_id);
            //updateObject copies the new field to existing fields , so in new list old values will be there
            List<ProductImage> newImages = imageBuilder.updateObject(existingProduct, objectMap);
            Product updatedProduct = existingProduct.toBuilder()
                    .images(newImages)
                    .build();
            dao.updateProduct(updatedProduct);
            return updatedProduct.getImages();
        } catch (Exception ex) {
            log.error("Exception while updating variant entry ", ex);
        }
        return null;
    }

    @Override
    public List<Product> fetchActiveProducts() {
        try {
            List<Product> products = dao.findActiveProducts();
            products = dao.updateProductStatus(products);
            return products;
        } catch (Exception ex) {
            log.error("Exception while updating the status of the product", ex);
        }
        return null;
    }

}
