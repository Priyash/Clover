package com.clover.spark.Model;

import lombok.Data;

import java.util.List;

@Data
public class ProductOption {
    private Long id;
    private Long product_id;
    private String name;
    private List<String> values;
}
