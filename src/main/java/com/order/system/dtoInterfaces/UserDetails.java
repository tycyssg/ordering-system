package com.order.system.dtoInterfaces;

public interface UserDetails {

    Integer getId();

    String getFirstName();

    String getSecondName();

    String getPhone();

    String getCurrentNumber();

    AddressUserDetails getAddress();
}
