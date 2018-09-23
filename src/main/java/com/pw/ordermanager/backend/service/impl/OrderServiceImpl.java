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
            List<Order> filteredOrders = filterOrdersByTitle(value, orders);
            filteredOrders = filterOrdersByStatus(value, orders, filteredOrders);
            orders = filteredOrders;
        }

        return orders;
    }

    private List<Order> filterOrdersByStatus(String value, List<Order> orders, List<Order> filteredOrders) {
        if(filteredOrders.isEmpty()){
            filteredOrders = orders.stream()
                    .filter(order -> StringUtils.containsIgnoreCase(order.getStatus().toString(), value))
                    .collect(Collectors.toList());
        }
        return filteredOrders;
    }

    private List<Order> filterOrdersByTitle(String value, List<Order> orders) {
        return orders.stream()
                        .filter(order -> StringUtils.containsIgnoreCase(order.getTitle(), value))
                        .collect(Collectors.toList());
    }

    @Override
    public Order findOrderById(Long id) {
        return orderRepository.findByOrderId(id);
    }

}
