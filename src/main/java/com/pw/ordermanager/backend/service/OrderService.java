package com.pw.ordermanager.backend.service;

import com.pw.ordermanager.backend.entity.Order;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;

public interface OrderService extends Serializable {

    void saveOrder(@NonNull Order order);

    void deleteOrder(@NonNull Order order);

    List<Order> findOrders(String value);
}
