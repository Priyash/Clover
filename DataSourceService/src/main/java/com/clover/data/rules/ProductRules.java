package com.clover.data.rules;

import com.clover.data.builder.OptionsBuilder;
import com.clover.data.builder.ProductVariantBuilder;
import com.clover.data.model.Option;
import com.clover.data.model.Product;
import com.clover.data.model.ProductVariant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Qualifier("productRule")
public class ProductRules implements IRule<Product, Map<String, Object>>{

    @Override
    public Product addRule(Map<String, Object> dataObject) {
        try {
            Product updatedProduct = (Product) dataObject.get("updatedProduct");
            ProductVariantBuilder variantBuilder = (ProductVariantBuilder) dataObject.get("variantBuilder");
            Map<String, Object> objectMap = (Map<String, Object>) dataObject.get("objectMap");
            OptionsBuilder optionsBuilder = (OptionsBuilder) dataObject.get("optionsBuilder");

            List<Option> options = (List<Option>) dataObject.get("options");

            if(updatedProduct.getVariants().isEmpty()) {
                List<ProductVariant> defaultVariants = variantBuilder.buildDefault(objectMap);
                updatedProduct.setVariants(defaultVariants);
                //build options
                if(options.size() == 0){
                    List<Option> defaultProductOptions = optionsBuilder.buildDefault(objectMap);
                    updatedProduct.setOptions(defaultProductOptions);
                }
            }
            return updatedProduct;
        } catch (Exception ex) {
            log.error("Exception while adding the rule : \" Default variant and default option \"", ex);
        }
        return null;
    }

}
