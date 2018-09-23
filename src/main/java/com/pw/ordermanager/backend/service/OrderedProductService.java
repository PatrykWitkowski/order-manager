package com.pw.ordermanager.backend.service;

import com.pw.ordermanager.backend.entity.Order;
import com.pw.ordermanager.backend.entity.OrderedProduct;
import lombok.NonNull;

import java.util.List;

public interface OrderedProductService {

    void save(@NonNull OrderedProduct orderedProduct);

    void delete(@NonNull OrderedProduct orderedProduct);

    List<OrderedProduct> findByOrder(@NonNull Order order);
}
