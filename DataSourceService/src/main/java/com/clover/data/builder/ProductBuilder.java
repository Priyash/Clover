package com.clover.data.builder;

import com.clover.data.model.Option;
import com.clover.data.model.Product;
import com.clover.data.model.ProductImage;
import com.clover.data.model.ProductVariant;
import com.clover.data.rules.IRule;
import com.clover.data.utility.Generator;
import com.clover.data.utility.ProductStatus;
import com.clover.data.validation.ConstraintValidator;
import com.clover.data.validation.ValidationResult;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class ProductBuilder implements Builder<Product,Product>{
    @Autowired
    private Gson gson;

    @Autowired
    @Qualifier("sequenceGenerator")
    private Generator sequenceGenerator;

    @Autowired
    @Qualifier("timestampGenerator")
    private Generator timeStampGenerator;

    @Autowired
    private OptionsBuilder optionsBuilder;

    @Autowired
    private ProductImageBuilder imageBuilder;

    @Autowired
    private ProductVariantBuilder variantBuilder;

    @Autowired
    @Qualifier("productRule")
    private IRule productRule;

    @Autowired
    @Qualifier("productVariantRule")
    private IRule productVariantRule;

    @Autowired
    @Qualifier("ProductTitleValidator")
    private ConstraintValidator productTitleValidator;

    @Override
    public Product build(Map<String, Object> objectMap) {
        try {
            List<Option> options = optionsBuilder.build(objectMap);
            List<ProductVariant> variants = variantBuilder.build(objectMap);
            List<ProductImage> images = imageBuilder.build(objectMap);
            String productJsonMap = gson.toJson(objectMap);

            Product product = gson.fromJson(productJsonMap, Product.class);
            Product updatedProduct = product.toBuilder()
                                            .images(images)
                                            .options(options)
                                            .variants(variants)
                                            .id((Long) objectMap.get("product_id"))
                                            .status(ProductStatus.ACTIVE)
                                            .created_at((String) timeStampGenerator.generate())
                                            .updated_at((String) timeStampGenerator.generate())
                                            .build();

            ValidationResult result = productTitleValidator.validate(updatedProduct.getTitle());
            if(!result.getIsValid()) {
                log.error("Product validation error: {}", Arrays.toString(result.getValidationMessages().toArray()));
                throw new IllegalStateException("Product validation failed");
            }

            Map<String, Object> productDataMap = new HashMap<>();
            productDataMap.put("updatedProduct", updatedProduct);
            productDataMap.put("variantBuilder", variantBuilder);
            productDataMap.put("objectMap", objectMap);
            productDataMap.put("optionsBuilder", optionsBuilder);
            productDataMap.put("options", options);

            //Adding rules for extra logic for default
            updatedProduct = (Product) productRule.addRule(productDataMap);
            //Adding rules for mapping the images with variant
            updatedProduct = (Product) productVariantRule.addRule(productDataMap);

            return updatedProduct;
        } catch (Exception ex) {
            log.error("Exception while building Product ", ex);
        }
        return null;
    }

    @Override
    public Product buildDefault(Map<String, Object> objectMap) {
        return null;
    }

    @Override
    public Product updateObject(Product product, Map<String, Object> objectMap) {
        return null;
    }
}
