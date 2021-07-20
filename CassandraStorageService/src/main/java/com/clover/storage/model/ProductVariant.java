package com.clover.storage.model;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.io.Serializable;


@Data
@UserDefinedType
public class ProductVariant implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(value = "title")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String title;

    @Column(value = "id")
    @CassandraType(type = CassandraType.Name.BIGINT)
    private Long id;

    @Column(value = "product_id")
    @CassandraType(type = CassandraType.Name.BIGINT)
    private Long product_id;

    @Column(value = "sku")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String sku;

    @Column(value = "created_at")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String created_at;

    @Column(value = "updated_at")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String updated_at;

    @Column(value = "price")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String price;

    @Column(value = "option1")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String option1;

    @Column(value = "option2")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String option2;

    @Column(value = "option3")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String option3;
}
