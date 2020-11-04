package com.order.system.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "ITEMS_TO_BUY")
@Data
public class ItemsToBuy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer itemId;
    private Integer quantity;
    private Integer cartId;
}
