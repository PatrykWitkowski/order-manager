package com.pw.ordermanager.backend.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "sellers")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellerId;

    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User owner;

    @OneToOne(fetch=FetchType.LAZY, mappedBy="seller")
    private OrderedProduct order;

    @NotBlank
    @Column(unique = true)
    private String name;

    private String description;

    private String sellerWebsiteUrl;

    @Override
    public String toString(){
        return name;
    }
}
