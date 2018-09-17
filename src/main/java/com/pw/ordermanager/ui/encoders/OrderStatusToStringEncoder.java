package com.pw.ordermanager.ui.encoders;

import com.pw.ordermanager.backend.common.OrderStatus;
import com.vaadin.flow.templatemodel.ModelEncoder;

public class OrderStatusToStringEncoder implements ModelEncoder<OrderStatus, String> {

    @Override
    public String encode(OrderStatus orderStatus) {
        if(orderStatus == null){
            return null;
        }
        return orderStatus.name();
    }

    @Override
    public OrderStatus decode(String s) {
        return OrderStatus.valueOf(s);
    }
}
