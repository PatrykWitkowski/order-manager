package com.pw.ordermanager.backend.service;

import com.pw.ordermanager.backend.entity.Order;
import com.pw.ordermanager.backend.entity.OrderedProduct;

import java.util.List;

public interface OrderedProductService {

    void save(OrderedProduct orderedProduct);

    void delete(OrderedProduct orderedProduct);

    boolean checkIfOrderedProductIsUnique(List<OrderedProduct> product, OrderedProduct orderedProduct);

    List<OrderedProduct> findByOrder(Order order);
}
