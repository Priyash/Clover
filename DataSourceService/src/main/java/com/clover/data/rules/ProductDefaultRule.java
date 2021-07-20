package com.clover.data.rules;

import com.clover.data.builder.ProductOptionBuilder;
import com.clover.data.builder.ProductImageBuilder;
import com.clover.data.builder.ProductVariantBuilder;
import com.clover.data.model.ProductOption;
import com.clover.data.model.Product;
import com.clover.data.model.ProductImage;
import com.clover.data.model.ProductVariant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Qualifier("productRule")
public class ProductDefaultRule implements IRule<Product, Map<String, Object>>{

    @Override
    public Product addRule(Map<String, Object> dataObject) {
        try {
            Product updatedProduct = (Product) dataObject.get("product");
            ProductVariantBuilder variantBuilder = (ProductVariantBuilder) dataObject.get("variantBuilder");
            Map<String, Object> objectMap = (Map<String, Object>) dataObject.get("objectMap");
            ProductOptionBuilder productOptionBuilder = (ProductOptionBuilder) dataObject.get("optionsBuilder");
            ProductImageBuilder imagesBuilder = (ProductImageBuilder) dataObject.get("imagesBuilder");

            List<ProductOption> productOptions = (List<ProductOption>) dataObject.get("options");
            List<ProductOption> images = (List<ProductOption>) dataObject.get("images");

            if(ObjectUtils.isEmpty(updatedProduct.getVariants())) {
                List<ProductVariant> defaultVariants = variantBuilder.buildDefault(objectMap);
                updatedProduct.setVariants(defaultVariants);
                //build options
                if(productOptions.size() == 0){
                    List<ProductOption> defaultProductProductOptions = productOptionBuilder.buildDefault(objectMap);
                    updatedProduct.setOptions(defaultProductProductOptions);
                }
                if(images.size() == 0) {
                    List<ProductImage> defaultProductImages = imagesBuilder.buildDefault(objectMap);
                    updatedProduct.setImages(defaultProductImages);
                }
            }
            return updatedProduct;
        } catch (Exception ex) {
            log.error("[Product Rule]Exception while adding the rule : \" Default variant and default option \"", ex);
        }
        return null;
    }

}
