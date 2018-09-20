package com.pw.ordermanager.backend.entity;

import lombok.Data;

@Data
public class Seller {

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Nullable
    //@OneToMany(mappedBy="seller")
    //private List<Item> itemsOfSeller;

    //@NotBlank
    //private String code;

    //@NotBlank
    private String name;

    private String description;

    private String sellerWebsiteUrl;
}
