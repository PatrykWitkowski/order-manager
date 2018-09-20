package com.pw.ordermanager.backend.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Data
//@Entity
public class OrderedProduct {

    private Long id;

    //@OneToOne
    private Product product;

    //@OneToOne
    private Seller seller;

    private Long amount;

    private double price;
}
