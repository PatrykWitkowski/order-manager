package com.pw.ordermanager.backend.service.impl;

import com.pw.ordermanager.backend.entity.Order;
import com.pw.ordermanager.backend.entity.OrderedProduct;
import com.pw.ordermanager.backend.entity.User;
import com.pw.ordermanager.backend.jpa.OrderRepository;
import com.pw.ordermanager.backend.jpa.OrderedProductRepository;
import com.pw.ordermanager.backend.jpa.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderedProductRepository orderedProductRepository;

    @Override
    public void saveOrder(Order order) {
        //userRepository.save(order.getOwner());
        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Order order) {
        //userRepository.delete(order.getOwner());
        orderRepository.delete(order);
    }

    @Override
    public List<Order> findOrders(User user, String value) {
        List<Order> orders = orderRepository.findByOwner(user);

        if(!StringUtils.isEmpty(value)){
            // by name
            List<Order> filteredOrders = orders.stream()
                    .filter(order -> StringUtils.equals(order.getTitle(), value))
                    .collect(Collectors.toList());
            // by status
            if(filteredOrders.isEmpty()){
                filteredOrders = orders.stream()
                        .filter(order -> StringUtils.equals(order.getStatus().toString(), value))
                        .collect(Collectors.toList());
            }
            orders = filteredOrders;
        }

        return orders;
    }

    @Override
    public Order findOrderById(Long id) {
        final Order byOrderId = orderRepository.findByOrderId(id);
        return byOrderId;
    }

}
