package com.order.system.repositories;

import com.order.system.dtoInterfaces.UserDetails;
import com.order.system.dtoInterfaces.UserMarketingDetails;
import com.order.system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

        User findByEmail(String email);

        User findByToken(String token);

        boolean existsByEmail(String email);

        boolean existsByToken(String token);

        boolean existsByCurrentNumber(String currentNumber);

        UserDetails findByCurrentNumber(String currentNumber);

        List<UserMarketingDetails> findAllUserMarketingDetailsBy();

        @Modifying
        @Query("update User u set u.currentNumber = ?1 where u.token = ?2")
        int updateOrderNumber(String currentNumber, String token);

}
