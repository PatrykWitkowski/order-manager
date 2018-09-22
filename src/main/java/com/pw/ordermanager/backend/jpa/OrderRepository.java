package com.pw.ordermanager.backend.jpa;

import com.pw.ordermanager.backend.entity.Order;
import com.pw.ordermanager.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long>, Serializable {

    List<Order> findByOwner(User owenr);

    Order findByOrderId(Long ig);
}
