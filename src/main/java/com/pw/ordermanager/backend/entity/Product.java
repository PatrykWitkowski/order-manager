package com.pw.ordermanager.backend.entity;

import lombok.Data;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Data
public class Product {

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Nullable
    //@OneToMany(mappedBy="product")
    //private List<Item> itemsOfProduct;

    //@NotBlank
    private String code;

    //@NotBlank
    // max 13
    private String name;

    private String description;

    private Map<Seller, Double> prices;

    //private String productWebsiteUrl;

    @Override
    public String toString(){
        return name;
    }
}
