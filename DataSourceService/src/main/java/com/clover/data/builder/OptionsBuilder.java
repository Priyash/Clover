package com.clover.data.builder;

import com.clover.data.model.Option;
import com.clover.data.model.Product;
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
public class OptionsBuilder implements Builder<List<Option>, Product>{

    @Autowired
    private Gson gson;

    @Autowired
    @Qualifier("sequenceGenerator")
    private Generator generator;

    @Override
    public List<Option> build(Map<String, Object> objectMap) {
        List<Option> productOptions = new ArrayList<>();
        try {
            if(!ObjectUtils.isEmpty(objectMap)) {
                List<Map<String, Object>> options = (List<Map<String, Object>>) objectMap.get("options");
                if(!ObjectUtils.isEmpty(options)) {
                    options.forEach(option -> {
                        String optionsJsonMap = gson.toJson(option);
                        Option productOption = gson.fromJson(optionsJsonMap, Option.class);
                        Option updatedProductOption = productOption.toBuilder()
                                .id((Long) generator.generate())
                                .product_id((Long) objectMap.get("product_id"))
                                .build();
                        productOptions.add(updatedProductOption);
                    });
                }
                return productOptions;
            }
        } catch (Exception ex) {
            log.error("Exception while building options object", ex);
        }
        return productOptions;
    }

    @Override
    public List<Option> buildDefault(Map<String, Object> objectMap) {
        List<Option> defaultProductOptions = new ArrayList<>();
        try {
            List<String> defaultOptionValues = new ArrayList<>();
            defaultOptionValues.add("default values");
            Option defaultProductOption = Option.builder()
                                                .product_id((Long) objectMap.get("product_id"))
                                                .id((Long) generator.generate())
                                                .name("default option title")
                                                .values(defaultOptionValues)
                                                .build();
            defaultProductOptions.add(defaultProductOption);
            return  defaultProductOptions;
        } catch (Exception ex) {
            log.error("Exception while building the list of product options ", ex);
        }
        return null;
    }


    //https://stackoverflow.com/questions/55634921/how-to-merge-two-objects-in-java-spring-boot-application/55636089#:~:text=var%20project%20%3D%20new%20Project%20%7B%20Name,value%20for%20the%20Description%20field.
    @Override
    public List<Option> updateObject(Product product, Map<String, Object> objectMap) {
        List<Option> optionList = new ArrayList<>();
        try {
            List<Map<String, Object>> sourceOptions = (List<Map<String, Object>>) objectMap.get("options");
            if(!ObjectUtils.isEmpty(sourceOptions)) {
                sourceOptions.forEach(option -> {
                    String optionsJsonMap = gson.toJson(option);
                    Option updatedProductOption = gson.fromJson(optionsJsonMap, Option.class);
                    optionList.add(updatedProductOption);
                });
            }
            return optionList;
        } catch (Exception ex) {
            log.error("Exception while updating the list of product options ", ex);
        }
        return null;
    }
}
