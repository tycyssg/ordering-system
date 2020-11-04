package com.order.system.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "CART")
@Data
public class Cart {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cartId")
    Set<ItemsToBuy> itemsToBuy;
    @Transient
    List<MenuItems> itemsToDisplay;
    @Transient
    Double total;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
