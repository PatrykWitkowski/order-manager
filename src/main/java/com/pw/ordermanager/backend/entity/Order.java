package com.pw.ordermanager.backend.entity;

import com.pw.ordermanager.backend.common.OrderStatus;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class Order implements Serializable {

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotNull
    //@ManyToOne(fetch=FetchType.LAZY)
    //@JoinColumn(name="OWNER_ID")
    private User owner;

    private String title;

    //@NotBlank
    //@Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    //@NotNull
    //@OneToOne
    private List<OrderedProduct> orderedProduct;

    //@NotNull
    private Long amount;

    /**
     * Depends from status:
     * planned - date of planned order
     * ordered - date of order
     * delivered - date of delivery
     * cancelled - date of cancellation
     */
    private LocalDate date;

    private String description;

    private double totalPrice;

    private Long counter;

}
