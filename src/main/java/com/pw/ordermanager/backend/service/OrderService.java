package com.pw.ordermanager.backend.service;

import com.pw.ordermanager.backend.entity.Order;
import com.pw.ordermanager.backend.entity.User;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;

public interface OrderService extends Serializable {

    void saveOrder(@NonNull Order order);

    void deleteOrder(@NonNull Order order);

    List<Order> findOrders(@NonNull User user, String value);

    Order findOrderById(@NonNull Long id);
}
