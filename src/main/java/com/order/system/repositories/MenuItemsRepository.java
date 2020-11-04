package com.order.system.repositories;

import com.order.system.models.MenuItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemsRepository extends JpaRepository<MenuItems, Integer> {

}
