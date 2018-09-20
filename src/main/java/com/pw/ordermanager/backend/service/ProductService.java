package com.pw.ordermanager.backend.service;

import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.Seller;

import java.util.List;

public interface ProductService {

    List<Product> findAllProducts();

    List<Product> findProductsBySeller(Seller seller);
}
