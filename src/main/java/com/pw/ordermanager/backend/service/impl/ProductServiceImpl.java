package com.pw.ordermanager.backend.service.impl;

import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.User;
import com.pw.ordermanager.backend.jpa.ProductRepository;
import com.pw.ordermanager.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> findProducts(User user, String value) {
        return productRepository.findByOwner(user);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }
}
