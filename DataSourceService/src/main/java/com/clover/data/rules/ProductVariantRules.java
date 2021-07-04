package com.clover.data.rules;

import com.clover.data.model.Product;
import com.clover.data.model.ProductImage;
import com.clover.data.model.ProductVariant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Qualifier("productVariantRule")
public class ProductVariantRules implements IRule<Product, Map<String, Object>>{


    @Override
    public Product addRule(Map<String, Object> dataObject) {
        try {
            Product updatedProduct = (Product) dataObject.get("updatedProduct");
            if(!ObjectUtils.isEmpty(updatedProduct) && !updatedProduct.getVariants().isEmpty() && !updatedProduct.getImages().isEmpty()) {
                List<Long> image_variant_ids = new ArrayList<>();
                for(ProductVariant variant : updatedProduct.getVariants()) {
                    for(ProductImage image : updatedProduct.getImages()) {
                        if(variant.getId().equals(image.getVariant_id())) {
                            image_variant_ids.add(image.getId());
                        }
                    }
                    variant.setVariant_image_ids(image_variant_ids);
                }
            }
            return updatedProduct;
        } catch (Exception ex) {
            log.error("Exception while adding the rule : \" mapping images ids to variant \"", ex);
        }
        return null;
    }
}
