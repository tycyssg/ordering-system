package com.order.system.models;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CompleteOrder {
    @NotNull
    private String cardNumber;
    @NotNull
    private String expireDate;
    @NotNull
    private String csv;
    @NotNull
    private String name;
    @NotNull
    private Double totalToPay;
}
