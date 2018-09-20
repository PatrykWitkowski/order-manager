package com.pw.ordermanager.backend.service;

import com.pw.ordermanager.backend.entity.OrderedProduct;

import java.util.List;

public interface OrderedProductService {

    List<OrderedProduct> findOrderedProduct();

    void save(OrderedProduct orderedProduct);

    void delete(OrderedProduct orderedProduct);

}
