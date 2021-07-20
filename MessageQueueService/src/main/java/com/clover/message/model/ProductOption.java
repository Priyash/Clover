package com.clover.message.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ProductOption {
    private Long id;
    private Long product_id;
    private String name;
    private List<String> values;
}
