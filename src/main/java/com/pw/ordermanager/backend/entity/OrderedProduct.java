package com.pw.ordermanager.backend.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
@Table(name = "ordered_products")
@EqualsAndHashCode(exclude={"product", "seller", "order"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"order", "product", "seller"})
public class OrderedProduct implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderedProductId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="seller_id")
    private Seller seller;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="order_id")
    private Order order;

    @NotNull
    private Long amount;

    @NotNull
    private double price;
}
