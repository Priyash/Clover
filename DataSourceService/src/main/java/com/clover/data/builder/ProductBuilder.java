package com.clover.data.builder;

import com.clover.data.model.ProductOption;
import com.clover.data.model.Product;
import com.clover.data.model.ProductImage;
import com.clover.data.model.ProductVariant;
import com.clover.data.rules.IRule;
import com.clover.data.utility.CopyFields;
import com.clover.data.utility.Generator;
import com.clover.data.utility.ProductStatus;
import com.clover.data.validation.ConstraintValidator;
import com.clover.data.validation.ValidationResult;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
    private Generator<String> timeStampGenerator;

    @Autowired
    private ProductOptionBuilder productOptionBuilder;

    @Autowired
    private ProductImageBuilder imageBuilder;

    @Autowired
    private ProductVariantBuilder variantBuilder;

    @Autowired
    @Qualifier("productRule")
    private IRule<Product, Map<String, Object>> productDefaultRule;

    @Autowired
    @Qualifier("productUpdateRule")
    private IRule<Product, Map<String, Object>> productUpdateRule;

    @Autowired
    @Qualifier("ProductTitleValidator")
    private ConstraintValidator<String> productTitleValidator;

    @Override
    public Product build(Map<String, Object> objectMap) {
        try {
            List<ProductOption> productOptions = productOptionBuilder.build(objectMap);
            List<ProductVariant> variants = variantBuilder.build(objectMap);
            List<ProductImage> images = imageBuilder.build(objectMap);
            String productJsonMap = gson.toJson(objectMap);

            Product product = gson.fromJson(productJsonMap, Product.class);
            Product updatedProduct = product.toBuilder()
                                            .images(images)
                                            .options(productOptions)
                                            .variants(variants)
                                            .id((Long) objectMap.get("product_id"))
                                            .status(ProductStatus.ACTIVE)
                                            .created_at(timeStampGenerator.generate())
                                            .updated_at(timeStampGenerator.generate())
                                            .build();

            ValidationResult result = productTitleValidator.validate(updatedProduct.getTitle());
            if(!result.getIsValid()) {
                log.error("Product validation error: {}", Arrays.toString(result.getValidationMessages().toArray()));
                throw new IllegalStateException("Product validation failed");
            }

            Map<String, Object> productDataMap = new HashMap<>();
            productDataMap.put("product", updatedProduct);
            productDataMap.put("variantBuilder", variantBuilder);
            productDataMap.put("imagesBuilder", imageBuilder);
            productDataMap.put("objectMap", objectMap);
            productDataMap.put("optionsBuilder", productOptionBuilder);
            productDataMap.put("options", productOptions);
            productDataMap.put("images", images);

            //Adding rules for extra logic for default
            updatedProduct = productDefaultRule.addRule(productDataMap);

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
    public Product updateObject(Product dstProduct, Map<String, Object> objectMap) {
        try {
            List<ProductOption> updatedProductOptions = productOptionBuilder.updateObject(dstProduct, objectMap);

            String productJsonMap = gson.toJson(objectMap);
            Product srcProduct = gson.fromJson(productJsonMap, Product.class);

            BeanUtils.copyProperties(srcProduct, dstProduct, CopyFields.getNullPropertyNames(srcProduct));
            Map<String, Object> productDataMap = new HashMap<>();

            productDataMap.put("product", dstProduct);
            productDataMap.put("options", updatedProductOptions);
            productDataMap.put("objectMap", objectMap);

            //Update product list rule
            dstProduct = productUpdateRule.addRule(productDataMap);

            return dstProduct;
        } catch (Exception ex) {
            log.error("Exception while updating the product object ", ex);
        }
        return null;
    }
}
