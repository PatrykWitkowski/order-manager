package com.pw.ordermanager.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Currency;


public class Item {

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotNull
    //@ManyToOne(fetch= FetchType.LAZY)
    //@JoinColumn(name="PRODUCT_ID")
    private Product product;

    //@NotNull
    //@ManyToOne(fetch=FetchType.LAZY)
    //@JoinColumn(name="SELLER_ID")
    private Seller seller;

    //@NotNull
    private Double price;

    private Currency currency;
}
