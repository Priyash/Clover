package com.clover.data.rules;

import com.clover.data.model.ProductOption;
import com.clover.data.model.Product;
import com.clover.data.model.ProductImage;
import com.clover.data.model.ProductVariant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Qualifier("productUpdateRule")
public class ProductUpdateRule implements IRule<Product, Map<String, Object>>{

    @Override
    public Product addRule(Map<String, Object> objectMap) {
        try{
            Product dstProduct = (Product) objectMap.get("product");
            List<ProductImage> updatedImages = (List<ProductImage>) objectMap.get("images");
            List<ProductVariant> updatedVariants = (List<ProductVariant>) objectMap.get("variants");
            List<ProductOption> updatedProductOptions = (List<ProductOption>) objectMap.get("options");

            if(!ObjectUtils.isEmpty(updatedVariants)) {
                dstProduct.setVariants(updatedVariants);
            }

            if(!ObjectUtils.isEmpty(updatedProductOptions)) {
                dstProduct.setOptions(updatedProductOptions);
            }

            if(!ObjectUtils.isEmpty(updatedImages)) {
                dstProduct.setImages(updatedImages);
            }
            return dstProduct;
        }catch (Exception ex) {
            log.error("[Product Update Rule]Exception while updating options, images and product variants", ex);
        }
        return null;
    }
}
