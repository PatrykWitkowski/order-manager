package com.pw.ordermanager.backend.jpa;

import com.pw.ordermanager.backend.entity.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct,Long>, Serializable {

}
