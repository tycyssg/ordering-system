package com.order.system.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "ADDRESSES")
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String addressLineOne;
    private String addressLineTwo;
    private String city;
    private String county;
    private String eircode;
}
