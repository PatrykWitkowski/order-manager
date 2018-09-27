package com.pw.ordermanager.backend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "products")
@EqualsAndHashCode(exclude={"owner", "order"})
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotNull
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User owner;

    @OneToMany(fetch=FetchType.EAGER, mappedBy="product")
    private List<OrderedProduct> order;

    @NotBlank
    @Column(length = 12)
    private String name;

    @NotBlank
    @Column(length = 20)
    private String type;

    private String description;

    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name="seller_prices")
    @MapKeyJoinColumn(name="seller_id")
    @Column(name="prices")
    private Map<Seller, Double> prices;

    private String productWebsiteUrl;

    public Product(){
        prices = new HashMap<>();
        order = new ArrayList<>();
    }

    public Product(User user) {
        this();
        this.owner = user;
    }

    @Override
    public String toString(){
        return name;
    }
}
