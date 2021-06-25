package com.clover.storage.service;

import com.clover.storage.dao.ProductDAO;
import com.clover.storage.model.Product;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private ProductDAO productDAO;

    @Override
    public void createProduct(Product product) {
        try {
            if(!ObjectUtils.isEmpty(product)){
                productDAO.createProduct(product);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
