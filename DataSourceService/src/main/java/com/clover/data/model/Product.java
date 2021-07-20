package com.clover.data.model;

import com.clover.data.utility.Constants;
import com.clover.data.utility.ProductStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder(toBuilder = true)
@Document(collection = Constants.MONGO_TBL)
public class Product {
    @Id
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
