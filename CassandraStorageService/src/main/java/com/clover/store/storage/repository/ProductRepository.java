package com.clover.store.storage.repository;

import com.clover.store.storage.model.Product;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CassandraRepository<Product,Long> {

}
