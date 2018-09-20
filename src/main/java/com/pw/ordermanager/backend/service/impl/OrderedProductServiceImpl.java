package com.pw.ordermanager.backend.service.impl;

import com.pw.ordermanager.backend.entity.OrderedProduct;
import com.pw.ordermanager.backend.service.OrderedProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderedProductServiceImpl implements OrderedProductService {

    @Override
    public List<OrderedProduct> findOrderedProduct() {
        return null;
    }

    @Override
    public void save(OrderedProduct orderedProduct) {

    }

    @Override
    public void delete(OrderedProduct orderedProduct) {

    }
}
