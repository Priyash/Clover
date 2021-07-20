package com.clover.spark.Model;

import lombok.Data;

import java.util.List;

@Data
public class Product {
    private Long id;
    private String title;
    private String description;
    private String created_at;
    private String updated_at;
    private String vendor;
    private ProductStatus status;

    private List<ProductOption> options;
    private List<ProductVariant> variants;
    private List<ProductImage> images;
}
