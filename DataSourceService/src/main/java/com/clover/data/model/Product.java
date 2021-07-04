package com.clover.data.model;

import com.clover.data.utility.ProductStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.util.List;

@Data
@Builder(toBuilder = true)
@Document(collection = "PRODUCT_TBL_TEST")
public class Product {
    @Id
    private Long id;
    private String title;
    private String description;
    @NonNull
    private String created_at;
    @NonNull
    private String updated_at;
    private String vendor;
    private ProductStatus status;

    private List<Option> options;
    private List<ProductVariant> variants;
    private List<ProductImage> images;
}
