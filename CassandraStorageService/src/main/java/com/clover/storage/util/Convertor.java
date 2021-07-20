package com.clover.storage.util;

import com.clover.storage.model.ProductImage;
import com.clover.storage.model.ProductOption;
import com.clover.storage.model.ProductVariant;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.UDTValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.catalyst.expressions.Conv;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Convertor {
    private static Convertor convertor = null;

    private Convertor() {}

    public static Convertor getInstance() {
        synchronized (Convertor.class) {
            if(convertor == null) {
                synchronized (Convertor.class) {
                    convertor = new Convertor();
                    return convertor;
                }
            }
            return convertor;
        }
    }



    public <T, V> T convert(V cassandraRow, Object object) {
        try {
            if (cassandraRow instanceof Row && object instanceof ProductImage) {
                return (T)this.convertFromCassandraToJavaProductImageList(cassandraRow);
            } else if(cassandraRow instanceof Row && object instanceof ProductOption){
                return(T) this.convertFromCassandraToJavaProductOptionList(cassandraRow);
            } else if(cassandraRow instanceof Row && object instanceof ProductVariant){
                return (T) this.convertFromCassandraToJavaProductVariantList(cassandraRow);
            }
        } catch (Exception ex) {
            log.error("[Convertor]Exception while converting image data to ProductImage ", ex);
        }
        return null;
    }

    private <T, V> T convertFromCassandraToJavaProductImageList(V cassandraRow) {
        List<ProductImage> productImages = new ArrayList<>();
        Row row = null;
        try {
            row = (Row) cassandraRow;
            List<UDTValue> imagesUDTValue = row.getList("images", UDTValue.class);
            imagesUDTValue.forEach(imageUDTValue -> {
                try {
                    ProductImage productImage = new ProductImage();
                    productImage.setPosition(imageUDTValue.getInt("position"));
                    productImage.setId(imageUDTValue.getLong("id"));
                    productImage.setCreated_at(imageUDTValue.getString("created_at"));
                    productImage.setCreated_at(imageUDTValue.getString("updated_at"));
                    productImage.setSrc(new URL(imageUDTValue.getString("src")));
                    productImage.setVariant_id(imageUDTValue.getLong("variant_id"));
                    productImage.setProduct_id(imageUDTValue.getLong("product_id"));
                    productImages.add(productImage);

                } catch (MalformedURLException e) {
                    log.error("[Convertor]MalformedURLException while converting image data to ProductImage ", e);
                } catch (Exception ex) {
                    log.error("[Convertor]Exception while converting image data to ProductImage ", ex);
                }
            });
            return (T) productImages;
        } catch (Exception ex) {
            log.error("[ProductRowReader]Exception while converting image data to ProductImage ", ex);
        }
        return null;
    }



    private <T, V> T convertFromCassandraToJavaProductOptionList(V cassandraRow) {
        List<ProductOption> productOptions = new ArrayList<>();
        Row row = null;
        try {
            row = (Row) cassandraRow;
            List<UDTValue> optionsUDTValue = row.getList("options", UDTValue.class);
            optionsUDTValue.forEach(optionUDTValue -> {
                ProductOption productOption = new ProductOption();
                productOption.setId(optionUDTValue.getLong("id"));
                productOption.setProduct_id(optionUDTValue.getLong("product_id"));
                productOption.setName(optionUDTValue.getString("name"));
                productOption.setValues(optionUDTValue.getList("values", String.class));
                productOptions.add(productOption);
            });
            return (T) productOptions;
        } catch (Exception ex) {
            log.error("[Convertor]Exception while converting option data to ProductOptions ", ex);
        }
        return null;
    }


    private <T, V> T convertFromCassandraToJavaProductVariantList(V cassandraRow) {
        List<ProductVariant> productVariants = new ArrayList<>();
        Row row = null;
        try {
            row = (Row) cassandraRow;
            List<UDTValue> optionsUDTValue = row.getList("variants", UDTValue.class);
            optionsUDTValue.forEach(optionUDTValue -> {
                ProductVariant productVariant = new ProductVariant();
                productVariant.setTitle(optionUDTValue.getString("title"));
                productVariant.setId(optionUDTValue.getLong("id"));
                productVariant.setProduct_id(optionUDTValue.getLong("product_id"));
                productVariant.setSku(optionUDTValue.getString("sku"));
                productVariant.setCreated_at(optionUDTValue.getString("created_at"));
                productVariant.setUpdated_at(optionUDTValue.getString("updated_at"));
                productVariant.setPrice(optionUDTValue.getString("price"));
                productVariant.setOption1(optionUDTValue.getString("option1"));
                productVariant.setOption2(optionUDTValue.getString("option2"));
                productVariant.setOption3(optionUDTValue.getString("option3"));
                productVariants.add(productVariant);
            });
            return (T) productVariants;
        } catch (Exception ex) {
            log.error("[Convertor]Exception while converting variants data to ProductVariant ", ex);
        }
        return null;
    }
}
