package com.order.system.services.interfaces;

import com.order.system.exceptions.models.InvalidCredentialsException;
import com.order.system.exceptions.models.UserNotFoundException;
import com.order.system.models.Address;
import com.order.system.models.MarketingMessage;
import com.order.system.models.User;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {

    public User saveUser(User user);

    public void logOut(String token);

    public User login(User user) throws UserNotFoundException, InvalidCredentialsException;

    public boolean verifyUserPermissions(String token, List<String> requiredPermission);

    public void updateAddress(Address address);

    public void sendMarketingMessages(MarketingMessage marketingMessage) throws MessagingException;
}
