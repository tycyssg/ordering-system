package com.order.system.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "MENUS")
@Data
public class Menu {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menuId")
    Set<MenuItems> items;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String menuName;
}
