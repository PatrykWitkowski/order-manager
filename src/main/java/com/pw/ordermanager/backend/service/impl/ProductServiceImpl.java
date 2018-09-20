package com.pw.ordermanager.backend.service.impl;

import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.backend.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public List<Product> findAllProducts() {
        return null;
    }

    @Override
    public List<Product> findProductsBySeller(Seller seller) {
        return null;
    }
}
