package com.pw.ordermanager.backend.service.impl;

import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.User;
import com.pw.ordermanager.backend.jpa.ProductRepository;
import com.pw.ordermanager.backend.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> findProducts(User user, String value) {
        List<Product> products = productRepository.findByOwner(user);

        if(StringUtils.isNotEmpty(value)){
            List<Product> filteredProducts = filterByName(value, products);

            if(filteredProducts.isEmpty()){
                filteredProducts = filterByType(value, products);

                if(filteredProducts.isEmpty()){
                    filteredProducts = filterBySellerName(value, products);
                }
            }

            products = filteredProducts;
        }

        return products;
    }

    private List<Product> filterBySellerName(String value, List<Product> products) {
        return products.stream()
                .filter(p -> p.getPrices().keySet().stream()
                        .anyMatch(seller -> StringUtils.containsIgnoreCase(seller.getName(), value)))
                .collect(Collectors.toList());
    }

    private List<Product> filterByType(String value, List<Product> products) {
        return products.stream()
                            .filter(p -> StringUtils.containsIgnoreCase(p.getType(), value))
                            .collect(Collectors.toList());
    }

    private List<Product> filterByName(String value, List<Product> products) {
        return products.stream()
                        .filter(p -> StringUtils.containsIgnoreCase(p.getName(), value))
                        .collect(Collectors.toList());
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
