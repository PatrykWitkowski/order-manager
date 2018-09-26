package com.pw.ordermanager.backend.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
@Data
public class Address {

    @NotBlank
    private String street;

    @NotBlank
    private String localNumber;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String location;
}
