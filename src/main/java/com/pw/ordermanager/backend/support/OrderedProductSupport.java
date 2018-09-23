package com.pw.ordermanager.backend.support;

import com.pw.ordermanager.backend.entity.OrderedProduct;

import java.util.List;
import java.util.Objects;

public class OrderedProductSupport {

    public static boolean checkIfOrderedProductIsUnique(List<OrderedProduct> products, OrderedProduct orderedProduct) {
        return products.stream()
                .noneMatch(p -> Objects.equals(p, orderedProduct));
    }
}
