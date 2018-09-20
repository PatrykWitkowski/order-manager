package com.pw.ordermanager.backend.service.impl;

import com.pw.ordermanager.backend.entity.OrderedProduct;
import com.pw.ordermanager.backend.service.OrderedProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderedProductServiceImpl implements OrderedProductService {

    private volatile static OrderedProductServiceImpl instance;

    private OrderedProductServiceImpl() {
    }

    public static OrderedProductServiceImpl getInstance() {
        if (instance == null) {
            synchronized (OrderedProductServiceImpl.class) {
                if (instance == null) {
                    instance = new OrderedProductServiceImpl();
                }
            }
        }

        return instance;
    }

    List<OrderedProduct> temp = new ArrayList<>();

    @Override
    public List<OrderedProduct> findOrderedProducts() {
        return temp;
    }

    @Override
    public void save(OrderedProduct orderedProduct) {
        temp.add(orderedProduct);
    }

    @Override
    public void delete(OrderedProduct orderedProduct) {
        temp.remove(orderedProduct);
    }

    @Override
    public boolean checkIfOrderedProductIsUnique(OrderedProduct orderedProduct) {
        return findOrderedProducts().stream()
                .noneMatch(p -> Objects.equals(p, orderedProduct));
    }
}
