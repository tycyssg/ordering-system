package com.order.system.repositories;

import com.order.system.dtoInterfaces.AddressUserDetails;
import com.order.system.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    AddressUserDetails findAddressUserDetailsById(Integer id);
}
