package com.order.system.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "MENU_ITEMS")
public class MenuItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double price;
    private String itemName;
    private String itemDescription;
    private Integer menuId;
    @Transient
    private Integer quantity;
}
