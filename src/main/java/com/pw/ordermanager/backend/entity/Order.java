package com.pw.ordermanager.backend.entity;

import com.pw.ordermanager.backend.common.OrderStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;


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
    private Item item;

    //@NotNull
    private Long amount;

    /**
     * Depends from status:
     * planned - date of planned order
     * ordered - date of order
     * delivered - date of delivery
     * cancelled - date of cancellation
     */
    private LocalDateTime dateTime;

    private String description;

}