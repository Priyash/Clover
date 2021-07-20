package com.clover.storage.model;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.List;

@Data
@Table("product_tbl_test")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKeyColumn(name = "id", type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.BIGINT)
    private Long id;

    @Column("title")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String title;

    @Column("description")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String description;

    @Column("created_at")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String created_at;

    @Column("updated_at")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String updated_at;

    @Column("vendor")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String vendor;

    @Column("status")
    @CassandraType(type = CassandraType.Name.TEXT)
    private ProductStatus status;

    @Column("options")
    private List<ProductOption> options;

    @Column("variants")
    private List<ProductVariant> variants;

    @Column("images")
    private List<ProductImage> images;
}
