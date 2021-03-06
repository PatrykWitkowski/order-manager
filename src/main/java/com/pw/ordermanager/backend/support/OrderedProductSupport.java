package com.pw.ordermanager.backend.support;

import com.pw.ordermanager.backend.entity.OrderedProduct;
import lombok.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class OrderedProductSupport {

    private OrderedProductSupport() {}

    public static boolean checkIfOrderedProductIsUnique(@NonNull List<OrderedProduct> products, OrderedProduct orderedProduct) {
        return products.stream()
                .noneMatch(p -> Objects.equals(p, orderedProduct));
    }

    public static double calculateTotalPrice(@NonNull Collection<OrderedProduct> orderedProducts){
        return orderedProducts.stream()
                .map(OrderedProduct::getPrice)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public static String priceFormat(double price){
        return String.format("%.2f", price);
    }
}
