package com.clover.data.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
public class ProductOption {
    private Long id;
    private Long product_id;
    private String name;
    private List<String> values;
}
