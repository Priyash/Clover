package com.clover.store.storage.service;

import com.clover.store.storage.dao.ProductDAO;
import com.clover.store.storage.model.Product;
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

    @Override
    public Product fetchProduct(String id) {
        try {
            if(!StringUtils.isEmpty(id)){
                Product fetchedProduct = productDAO.getProduct(id);
                if(!ObjectUtils.isEmpty(fetchedProduct)){
                    return fetchedProduct;
                } else {
                    throw new IllegalStateException("Product Object is null from fetchProduct method ");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
