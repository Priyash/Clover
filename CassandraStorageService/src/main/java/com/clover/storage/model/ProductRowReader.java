package com.clover.storage.model;

import com.clover.storage.util.Convertor;
import com.datastax.driver.core.Row;
import com.datastax.spark.connector.CassandraRowMetadata;
import com.datastax.spark.connector.ColumnRef;
import com.datastax.spark.connector.cql.TableDef;
import com.datastax.spark.connector.rdd.reader.RowReader;
import com.datastax.spark.connector.rdd.reader.RowReaderFactory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import scala.collection.IndexedSeq;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Component
public class ProductRowReader extends GenericRowReader<Product>{
    private static final long serialVersionUID = 1L;
    private static RowReader<Product> reader = new ProductRowReader();

    @Component
    public static class ProductRowReaderFactory implements RowReaderFactory<Product>, Serializable {
        private static final long serialVersionUID = 1L;
        @Override
        public RowReader<Product> rowReader(TableDef arg0, IndexedSeq<ColumnRef> arg1) {
            return reader;
        }

        @Override
        public Class<Product> targetClass() {
            return Product.class;
        }
    }


    @Override
    public Product read(Row row, CassandraRowMetadata cassandraRowMetadata) {
        Product product = new Product();
        product.setId(row.getLong("id"));
        product.setDescription(row.getString("description"));
        product.setVendor(row.getString("vendor"));
        product.setStatus(ProductStatus.valueOf(row.getString("status")));
        product.setCreated_at(row.getString("created_at"));
        product.setUpdated_at(row.getString("updated_at"));

        List<ProductImage> productImages = Convertor.getInstance().convert(row, new ProductImage());
        List<ProductOption> productOptions = Convertor.getInstance().convert(row, new ProductOption());
        List<ProductVariant> productVariants = Convertor.getInstance().convert(row, new ProductVariant());

        product.setImages(productImages);
        product.setOptions(productOptions);
        product.setVariants(productVariants);
        return product;
    }
}
