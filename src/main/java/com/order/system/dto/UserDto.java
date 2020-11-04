package com.order.system.dto;

import com.order.system.dtoInterfaces.AddressUserDetails;
import lombok.Data;

@Data
public class UserDto {
    public String fullName;
    public String phone;
    public String orderNumber;
    public AddressUserDetails addressUserDetails;
}
