package com.pw.ordermanager.backend.service.impl;

import com.pw.ordermanager.backend.common.OrderStatus;
import com.pw.ordermanager.backend.entity.Order;
import com.pw.ordermanager.backend.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public void saveOrder(Order order) {

    }

    @Override
    public void deleteOrder(Order order) {

    }

    @Override
    public List<Order> findOrders(String value) {
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setAmount(1L);
        order.setCounter(4L);
        order.setDate(LocalDate.now());
        order.setId(1L);
        order.setOrderStatus(OrderStatus.ORDERED);
        order.setTitle("New testing order #1");
        order.setTotalPrice(20.31);
        orders.add(order);
        return orders;
    }

}
