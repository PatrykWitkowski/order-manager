package com.pw.ordermanager.backend.entity;

import com.pw.ordermanager.backend.common.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
@EqualsAndHashCode(exclude="owner")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @NotNull
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne
    @JoinColumn(name="user_id")
    private User owner;

    @NotBlank
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private OrderStatus status;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order")
    private List<OrderedProduct> orderedProduct;

    /**
     * Depends from status:
     * planned - date of planned order
     * ordered - date of order
     * delivered - date of delivery
     * cancelled - date of cancellation
     */
    @NotNull
    private LocalDate orderDate;

    private String description;

    private double totalPrice;

    private Long counter;

    public Order(){
        orderedProduct = new ArrayList<>();
        this.counter = 0L;
    }

    public Order(User user) {
        this.counter = 0L;
        this.owner = user;
        orderedProduct = new ArrayList<>();
    }
}
