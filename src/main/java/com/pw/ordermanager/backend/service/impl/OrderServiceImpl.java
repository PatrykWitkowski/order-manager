package com.pw.ordermanager.backend.service.impl;

import com.pw.ordermanager.backend.entity.Order;
import com.pw.ordermanager.backend.entity.User;
import com.pw.ordermanager.backend.jpa.OrderRepository;
import com.pw.ordermanager.backend.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Order order) {
        orderRepository.delete(order);
    }

    @Override
    public List<Order> findOrders(User user, String value) {
        List<Order> orders = orderRepository.findByOwner(user);

        if(!StringUtils.isEmpty(value)){
            // by name
            List<Order> filteredOrders = orders.stream()
                    .filter(order -> StringUtils.containsIgnoreCase(order.getTitle(), value))
                    .collect(Collectors.toList());
            // by status
            if(filteredOrders.isEmpty()){
                filteredOrders = orders.stream()
                        .filter(order -> StringUtils.containsIgnoreCase(order.getStatus().toString(), value))
                        .collect(Collectors.toList());
            }
            orders = filteredOrders;
        }

        return orders;
    }

    @Override
    public Order findOrderById(Long id) {
        return orderRepository.findByOrderId(id);
    }

}
