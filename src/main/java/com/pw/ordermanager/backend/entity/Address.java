package com.pw.ordermanager.backend.entity;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class Address {

    private String street;

    private String localNumber;

    private String postalCode;

    private String location;
}
