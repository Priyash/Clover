package com.clover.spark.Model;

import lombok.Data;

import java.net.URL;

@Data
public class ProductImage {
    private Long id;
    private Integer position;
    private Long product_id;
    private Long variant_id;
    private String created_at;
    private String updated_at;
    private URL src;
}
