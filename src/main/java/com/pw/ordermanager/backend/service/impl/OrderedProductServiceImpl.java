package com.pw.ordermanager.backend.service.impl;

import com.pw.ordermanager.backend.entity.Order;
import com.pw.ordermanager.backend.entity.OrderedProduct;
import com.pw.ordermanager.backend.jpa.OrderedProductRepository;
import com.pw.ordermanager.backend.service.OrderedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderedProductServiceImpl implements OrderedProductService {

    @Autowired
    private OrderedProductRepository orderedProductRepository;

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


    @Override
    public void save(OrderedProduct orderedProduct) {
        orderedProductRepository.save(orderedProduct);
    }

    @Override
    public void delete(OrderedProduct orderedProduct) {
        orderedProductRepository.delete(orderedProduct);
    }

    @Override
    public boolean checkIfOrderedProductIsUnique(List<OrderedProduct> products, OrderedProduct orderedProduct) {
        return products.stream()
                .noneMatch(p -> Objects.equals(p, orderedProduct));
    }

    @Override
    public List<OrderedProduct> findByOrder(Order order) {
        return orderedProductRepository.findByOrder(order);
    }
}
