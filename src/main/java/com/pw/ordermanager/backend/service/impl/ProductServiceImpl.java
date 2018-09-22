package com.pw.ordermanager.backend.service.impl;

import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.backend.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private volatile static ProductServiceImpl instance;

    private ProductServiceImpl() {
    }

    public static ProductServiceImpl getInstance() {
        if (instance == null) {
            synchronized (ProductServiceImpl.class) {
                if (instance == null) {
                    instance = new ProductServiceImpl();
                }
            }
        }

        return instance;
    }

    List<Product> temp = new ArrayList<>();

    @Override
    public List<Product> findAllProducts() {
        Product p = new Product();
        p.setCode("CODE#1");
        p.setName("Temp product 1");
        Map<Seller, Double> prices = new HashMap<>();
        Seller seller = new Seller();
        seller.setName("Seller temp 1");
        prices.put(seller, 25.44);
        p.setPrices(prices);

        Product p2 = new Product();
        p2.setCode("CODE#2");
        p2.setName("Temp product 2");
        Map<Seller, Double> prices2 = new HashMap<>();
        Seller seller2 = new Seller();
        seller2.setName("Seller temp 2");
        prices2.put(seller2, 87.22);
        p2.setPrices(prices2);

        if(temp.isEmpty()){
            temp.add(p);
            temp.add(p2);
        }
        return temp;
    }

    @Override
    public Set<Product> findProductsBySeller(List<Product> products, Seller seller) {
        return products.stream()
                .filter(p -> p.getPrices().keySet().contains(seller))
                .collect(Collectors.toSet());
    }
}
