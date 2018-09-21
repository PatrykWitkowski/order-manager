package com.pw.ordermanager.backend.entity;

import com.pw.ordermanager.backend.common.OrderStatus;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User owner;

    @NotBlank
    private String title;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private OrderStatus status;

    @OneToMany(mappedBy = "order")
    private Set<OrderedProduct> orderedProduct;

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
}
