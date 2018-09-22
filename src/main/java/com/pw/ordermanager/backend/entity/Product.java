package com.pw.ordermanager.backend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
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

    @OneToOne(fetch=FetchType.EAGER, mappedBy="product")
    private OrderedProduct order;

    @NotBlank
    @Column(unique = true)
    private String code;

    @NotBlank
    @Column(length = 12)
    private String name;

    private String description;

    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name="seller_prices")
    @MapKeyJoinColumn(name="seller_id")
    @Column(name="prices")
    private Map<Seller, Double> prices;

    private String productWebsiteUrl;

    @Override
    public String toString(){
        return name;
    }
}
