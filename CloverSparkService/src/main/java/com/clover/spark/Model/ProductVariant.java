package com.clover.spark.Model;

import lombok.Data;


@Data
public class ProductVariant {
    private String title;
    private Long id;
    private Long product_id;
    private String sku;
    private String created_at;
    private String updated_at;
    private String price;
    private String option1;
    private String option2;
    private String option3;
}
