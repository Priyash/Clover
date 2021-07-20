package com.clover.data.builder;

import com.clover.data.model.Product;
import com.clover.data.model.ProductImage;
import com.clover.data.model.ProductVariant;
import com.clover.data.utility.CopyFields;
import com.clover.data.utility.Generator;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ProductImageBuilder implements Builder<List<ProductImage>, Product>{

    @Autowired
    private Gson gson;

    @Autowired
    @Qualifier("sequenceGenerator")
    private Generator sequenceGenerator;

    @Autowired
    @Qualifier("timestampGenerator")
    private Generator timeStampGenerator;

    @Override
    public List<ProductImage> build(Map<String, Object> objectMap) {
        List<ProductImage> images = new ArrayList<>();
        try {
            Long product_id = (Long) objectMap.get("product_id");
            List<Map<String, Object>> productImages = (List<Map<String, Object>>) objectMap.get("images");
            if(!ObjectUtils.isEmpty(productImages)) {
                productImages.forEach(image -> {
                    String productImageJsonMap = gson.toJson(image);
                    ProductImage productImage = gson.fromJson(productImageJsonMap, ProductImage.class);
                    ProductImage updatedProductImage = this.buildProductImage(productImage);
                    ProductImage finalProductImage = updatedProductImage.toBuilder()
                            .product_id(product_id)
                            .build();
                    images.add(finalProductImage);
                });
            }
            return images;
        } catch (Exception ex) {
            log.error("Exception while building the product image list ", ex);
        }
        return images;
    }

    @Override
    public List<ProductImage> buildDefault(Map<String, Object> objectMap) {
        Long product_id = (Long) objectMap.get("product_id");
        try {
            ProductImage defaultProductImage = ProductImage.builder()
                                                    .product_id(product_id)
                                                    .build();
            ProductImage defaultUpdatedProductImage = buildProductImage(defaultProductImage);
            List<ProductImage> defaultProductImages = new ArrayList<>();
            defaultProductImages.add(defaultUpdatedProductImage);
            return defaultProductImages;
        } catch (Exception ex) {
            log.error("Exception building default list of product image of id: {}", product_id, ex);
        }
        return null;
    }

    private ProductImage buildProductImage(ProductImage partialProductImage) {
        ProductImage updatedProductImage = null;
        try {
            updatedProductImage = partialProductImage.toBuilder()
                                                    .created_at((String) timeStampGenerator.generate())
                                                    .updated_at((String) timeStampGenerator.generate())
                                                    .id((Long) sequenceGenerator.generate())
                                                    .build();
            return updatedProductImage;
        } catch (Exception ex) {
            log.error("Exception while updating partially generated product image id: {}, product_id: {}",
                                            partialProductImage.getId(), partialProductImage.getProduct_id(), ex);
        }
        return null;
    }


    @Override
    public List<ProductImage> updateObject(Product product, Map<String, Object> objectMap) {
        try {
            List<ProductImage> dstImages = product.getImages();
            List<Map<String,Object>> srcImages = (List<Map<String, Object>>) objectMap.get("images");
            if(!ObjectUtils.isEmpty(srcImages) && !ObjectUtils.isEmpty(dstImages)){
                srcImages.forEach(srcImage -> {
                    dstImages.forEach(dstImage -> {
                        if(srcImage.get("id").equals(dstImage.getId())) {
                            String srcProductImageJsonMap = gson.toJson(srcImage);
                            ProductImage srcProductImage = gson.fromJson(srcProductImageJsonMap, ProductImage.class);
                            BeanUtils.copyProperties(srcProductImage, dstImage, CopyFields.getNullPropertyNames(srcProductImage));
                            dstImage.setUpdated_at((String) timeStampGenerator.generate());
                        }
                    });
                });
            }
            return dstImages;
        } catch (Exception ex) {

        }
        return null;
    }
}
