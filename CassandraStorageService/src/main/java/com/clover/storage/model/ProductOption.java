package com.clover.storage.model;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.io.Serializable;
import java.util.List;

@Data
@UserDefinedType
public class ProductOption implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(value = "id")
    @CassandraType(type = CassandraType.Name.BIGINT)
    private Long id;

    @Column(value = "product_id")
    @CassandraType(type = CassandraType.Name.BIGINT)
    private Long product_id;

    @Column(value = "name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String name;

    @Column(value = "values")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = {CassandraType.Name.TEXT})
    private List<String> values;
}
