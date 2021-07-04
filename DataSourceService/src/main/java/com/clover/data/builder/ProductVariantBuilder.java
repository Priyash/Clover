package com.clover.data.builder;

import com.clover.data.model.Option;
import com.clover.data.model.Product;
import com.clover.data.model.ProductVariant;
import com.clover.data.utility.Generator;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProductVariantBuilder implements Builder<List<ProductVariant>, Product>{

    @Autowired
    private Gson gson;

    @Autowired
    @Qualifier("sequenceGenerator")
    private Generator sequenceGenerator;

    @Autowired
    @Qualifier("timestampGenerator")
    private Generator timeStampGenerator;

    @Override
    public List<ProductVariant> build(Map<String, Object> objectMap) {
        List<ProductVariant> variants = new ArrayList<>();
        try {
            List<Map<String, Object>> productVariants = (List<Map<String, Object>>) objectMap.get("variants");
            if(!ObjectUtils.isEmpty(productVariants)) {
                productVariants.forEach(variant -> {
                    String productVariantJsonMap = gson.toJson(variant);
                    ProductVariant productVariant = gson.fromJson(productVariantJsonMap, ProductVariant.class);
                    ProductVariant updatedVariant = this.buildProductVariant(productVariant);
                    variants.add(updatedVariant);
                });
                return variants;
            }
        } catch (Exception ex) {
            log.error("Exception while building the list of variants ", ex);
        }
        return variants;
    }

    @Override
    public List<ProductVariant> buildDefault(Map<String, Object> objectMap) {
        List<ProductVariant> defaultVariants = new ArrayList<>();
        Long product_id = (Long) objectMap.get("product_id");
        try {
            ProductVariant defaultVariant = ProductVariant.builder()
                                                            .product_id(product_id)
                                                            .price("0.0")
                                                            .build();
            ProductVariant updatedDefaultVariant = this.buildProductVariant(defaultVariant);
            updatedDefaultVariant.setTitle("default_title");
            defaultVariants.add(updatedDefaultVariant);
            return defaultVariants;
        } catch (Exception ex) {
            log.error("Exception while building the default product variant ", ex);
        }
        return null;
    }

    @Override
    public List<ProductVariant> updateObject(Product product, Map<String, Object> objectMap) {
        return null;
    }

    private ProductVariant buildProductVariant(ProductVariant partialProductVariant) {
        try {
            Long product_variant_id = (Long) sequenceGenerator.generate();
            ProductVariant updatedProductVariant = partialProductVariant.toBuilder()
                                                    .id(product_variant_id)
                                                    .updated_at((String) timeStampGenerator.generate())
                                                    .created_at((String) timeStampGenerator.generate())
                                                    .title(generateTitleForProductVariantFromOptions(partialProductVariant))
                                                    .build();
            return updatedProductVariant;
        } catch (Exception ex) {
            log.error("Exception while updating the partially updated product variant id: {}, product_id: {}",
                                            partialProductVariant.getId(), partialProductVariant.getProduct_id(), ex);
        }
        return null;
    }

    private String generateTitleForProductVariantFromOptions(ProductVariant partialProductVariant) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(partialProductVariant.getOption1() + "-");
            builder.append(partialProductVariant.getOption2() + "-");
            builder.append(partialProductVariant.getOption3());
            return builder.toString();
        } catch (Exception ex) {
            log.error("Exception while generating the title of partially updated product variant of id: {}, product_id: {}",
                    partialProductVariant.getId(), partialProductVariant.getProduct_id(), ex);
        }
        return null;
    }
}
