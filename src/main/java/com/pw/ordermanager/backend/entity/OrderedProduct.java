package com.pw.ordermanager.backend.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Objects;

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
