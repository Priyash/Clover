package com.clover.data.builder;

import com.clover.data.model.ProductOption;
import com.clover.data.model.Product;
import com.clover.data.utility.CopyFields;
import com.clover.data.utility.Generator;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ProductOptionBuilder implements Builder<List<ProductOption>, Product>{

    @Autowired
    private Gson gson;

    @Autowired
    @Qualifier("sequenceGenerator")
    private Generator generator;

    @Override
    public List<ProductOption> build(Map<String, Object> objectMap) {
        List<ProductOption> productProductOptions = new ArrayList<>();
        try {
            if(!ObjectUtils.isEmpty(objectMap)) {
                List<Map<String, Object>> options = (List<Map<String, Object>>) objectMap.get("options");
                if(!ObjectUtils.isEmpty(options)) {
                    options.forEach(option -> {
                        String optionsJsonMap = gson.toJson(option);
                        ProductOption productOption = gson.fromJson(optionsJsonMap, ProductOption.class);
                        ProductOption updatedProductProductOption = productOption.toBuilder()
                                .id((Long) generator.generate())
                                .product_id((Long) objectMap.get("product_id"))
                                .build();
                        productProductOptions.add(updatedProductProductOption);
                    });
                }
                return productProductOptions;
            }
        } catch (Exception ex) {
            log.error("Exception while building options object", ex);
        }
        return productProductOptions;
    }

    @Override
    public List<ProductOption> buildDefault(Map<String, Object> objectMap) {
        List<ProductOption> defaultProductProductOptions = new ArrayList<>();
        try {
            List<String> defaultOptionValues = new ArrayList<>();
            defaultOptionValues.add("default values");
            ProductOption defaultProductProductOption = ProductOption.builder()
                                                .product_id((Long) objectMap.get("product_id"))
                                                .id((Long) generator.generate())
                                                .name("default option title")
                                                .values(defaultOptionValues)
                                                .build();
            defaultProductProductOptions.add(defaultProductProductOption);
            return defaultProductProductOptions;
        } catch (Exception ex) {
            log.error("Exception while building the list of product options ", ex);
        }
        return null;
    }


    //https://stackoverflow.com/questions/55634921/how-to-merge-two-objects-in-java-spring-boot-application/55636089#:~:text=var%20project%20%3D%20new%20Project%20%7B%20Name,value%20for%20the%20Description%20field.
    @Override
    public List<ProductOption> updateObject(Product product, Map<String, Object> objectMap) {

        try {
            List<Map<String, Object>> sourceOptions = (List<Map<String, Object>>) objectMap.get("options");
            List<ProductOption> dstProductOptions = product.getOptions();
            if(!ObjectUtils.isEmpty(sourceOptions) && !ObjectUtils.isEmpty(dstProductOptions)) {
                sourceOptions.forEach(srcOption -> {
                    dstProductOptions.forEach(dstOption -> {
                        if(srcOption.get("id").equals(dstOption.getId())) {
                            String srcOptionJsonMap = gson.toJson(srcOption);
                            ProductOption srcProductProductOption = gson.fromJson(srcOptionJsonMap, ProductOption.class);
                            BeanUtils.copyProperties(srcProductProductOption, dstOption, CopyFields.getNullPropertyNames(srcProductProductOption));
                        }
                    });
                });
            }
            return dstProductOptions;
        } catch (Exception ex) {
            log.error("Exception while updating the list of product options ", ex);
        }
        return null;
    }
}
