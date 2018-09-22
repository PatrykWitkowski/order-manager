package com.pw.ordermanager.backend.service;

import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.Seller;

import java.util.List;
import java.util.Set;

public interface ProductService {

    List<Product> findAllProducts();

    Set<Product> findProductsBySeller(List<Product> products, Seller seller);
}
