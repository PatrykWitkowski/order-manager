package com.pw.ordermanager.backend.service.impl;

import com.pw.ordermanager.backend.entity.Order;
import com.pw.ordermanager.backend.entity.OrderedProduct;
import com.pw.ordermanager.backend.jpa.OrderedProductRepository;
import com.pw.ordermanager.backend.service.OrderedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderedProductServiceImpl implements OrderedProductService {

    @Autowired
    private OrderedProductRepository orderedProductRepository;

    @Override
    public void save(OrderedProduct orderedProduct) {
        orderedProductRepository.save(orderedProduct);
    }

    @Override
    public void delete(OrderedProduct orderedProduct) {
        orderedProductRepository.delete(orderedProduct);
    }

    @Override
    public List<OrderedProduct> findByOrder(Order order) {
        return orderedProductRepository.findByOrder(order);
    }
}
