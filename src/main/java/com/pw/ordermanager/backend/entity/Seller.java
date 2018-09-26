package com.pw.ordermanager.backend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
@Table(name = "sellers")
@EqualsAndHashCode(exclude={"owner", "order"})
@NoArgsConstructor
public class Seller implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellerId;

    @NotNull
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User owner;

    @OneToOne(fetch=FetchType.EAGER, mappedBy="seller")
    private OrderedProduct order;

    @NotBlank
    @Column(unique = true, length = 13)
    private String nip;

    @NotBlank
    private String name;

    @NotNull
    @Embedded
    private Address address;

    private String description;

    public Seller(User user) {
        this.owner = user;
    }

    @Override
    public String toString(){
        return name;
    }
}
