package com.order.system.repositories;

import com.order.system.models.ItemsToBuy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsToBuyRepository extends JpaRepository<ItemsToBuy, Integer> {
    boolean existsByItemId(Integer itemId);
}
