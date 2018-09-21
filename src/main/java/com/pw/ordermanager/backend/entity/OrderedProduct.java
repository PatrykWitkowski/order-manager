package com.pw.ordermanager.backend.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@Entity
@Table(name = "ordered_products")
public class OrderedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderedProductId;

    @NotNull
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PRODUCT_ID")
    private Product product;

    @NotNull
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SELLER_ID")
    private Seller seller;

    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ORDER_ID")
    private Order order;

    @NotNull
    private Long amount;

    @NotNull
    private double price;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderedProduct that = (OrderedProduct) o;
        return Objects.equals(product, that.product) &&
                Objects.equals(seller, that.seller);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, seller);
    }
}
