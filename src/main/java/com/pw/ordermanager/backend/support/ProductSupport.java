package com.pw.ordermanager.backend.support;

import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.Seller;
import lombok.NonNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductSupport {

    private ProductSupport() {}

    public static Set<Product> findProductsBySeller(@NonNull List<Product> products, @NonNull Seller seller) {
        return products.stream()
                .filter(p -> p.getPrices().keySet().contains(seller))
                .collect(Collectors.toSet());
    }
}
