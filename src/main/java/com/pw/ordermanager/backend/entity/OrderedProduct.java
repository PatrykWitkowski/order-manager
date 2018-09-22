package com.pw.ordermanager.backend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
@Table(name = "ordered_products")
@EqualsAndHashCode(exclude={"product", "seller", "order"})
public class OrderedProduct implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderedProductId;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PRODUCT_ID")
    private Product product;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SELLER_ID")
    private Seller seller;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ORDER_ID")
    private Order order;

    @NotNull
    private Long amount;

    @NotNull
    private double price;
}
