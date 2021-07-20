package com.clover.storage.model;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.io.Serializable;
import java.net.URL;

@Data
@UserDefinedType
public class ProductImage implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(value = "id")
    @CassandraType(type = CassandraType.Name.BIGINT)
    private Long id;

    @Column(value = "position")
    @CassandraType(type = CassandraType.Name.INT)
    private Integer position;

    @Column(value = "product_id")
    @CassandraType(type = CassandraType.Name.BIGINT)
    private Long product_id;

    @Column(value = "variant_id")
    @CassandraType(type = CassandraType.Name.BIGINT)
    private Long variant_id;

    @Column(value = "created_at")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String created_at;

    @Column(value = "updated_at")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String updated_at;

    @Column(value = "src")
    @CassandraType(type = CassandraType.Name.TEXT)
    private URL src;
}
