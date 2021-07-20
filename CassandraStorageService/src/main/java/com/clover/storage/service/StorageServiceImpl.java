package com.clover.storage.service;

import com.clover.storage.dao.ProductDAO;
import com.clover.storage.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

    @Autowired
    private ProductDAO productDAO;

    @Override
    public void createProduct(Product product) {
        log.info("Product id: {}", product.getId());
        try {
            if(!ObjectUtils.isEmpty(product)){
                productDAO.createProduct(product);
            }
        } catch (Exception ex) {
            log.error("Exception while storing product from createMethod from StorageServiceImpl class ", ex);
        }
    }
}
