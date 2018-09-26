package com.pw.ordermanager.backend.service;

import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.User;
import lombok.NonNull;

import java.util.List;

public interface ProductService {

    List<Product> findProducts(@NonNull User user, String value);

    Product saveProduct(@NonNull Product product);

    void deleteProduct(@NonNull Product product);
}
