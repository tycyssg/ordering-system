package com.order.system.services;

import com.order.system.dtoInterfaces.UserMarketingDetails;
import com.order.system.exceptions.models.InvalidCredentialsException;
import com.order.system.exceptions.models.UserNotFoundException;
import com.order.system.models.*;
import com.order.system.repositories.AddressRepository;
import com.order.system.repositories.UserRepository;
import com.order.system.services.interfaces.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static com.order.system.constants.ErrorConstants.INVALID_CREDENTIALS;
import static com.order.system.constants.ErrorConstants.USER_NOT_FOUND;

@Service
@Transactional
public class UserServiceImplementation implements UserService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, AddressRepository addressRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.emailService = emailService;
    }

    @Override
    public User saveUser(User user) {
        long userRows = userRepository.count();

        if (userRows == 0) {
            user.setRoles(new HashSet<>(Collections.singletonList(new Roles("ADMIN"))));
        } else {
            user.setRoles(new HashSet<>(Collections.singletonList(new Roles("USER"))));
        }

        user.setAddress(new Address());
        user.setCart(new Cart());

        return userRepository.save(user);
    }

    @Override
    public void logOut(String token) {
        User dbUser = userRepository.findByToken(token);
        dbUser.setToken(StringUtils.EMPTY);
        userRepository.save(dbUser);
    }

    @Override
    public User login(User user) throws UserNotFoundException, InvalidCredentialsException {
        if (!userRepository.existsByEmail(user.getEmail()))
            throw new UserNotFoundException(USER_NOT_FOUND);

        User dbUser = userRepository.findByEmail(user.getEmail());

        if (!user.getPassword().equals(dbUser.getPassword()))
            throw new InvalidCredentialsException(INVALID_CREDENTIALS);

        dbUser.setToken(RandomStringUtils.randomAlphanumeric(64));
        dbUser = userRepository.save(dbUser);

        User userToReturn = new User();
        BeanUtils.copyProperties(dbUser, userToReturn);
        userToReturn.setPassword(StringUtils.EMPTY);

        return userToReturn;
    }

    @Override
    public boolean verifyUserPermissions(String token, List<String> requiredPermission) {
        User dbUser = userRepository.findByToken(token);

        for (String s : requiredPermission) {
            boolean hasPermission = dbUser.getRoles().stream().anyMatch(roles -> roles.getRole().equals(s));
            if (hasPermission) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void updateAddress(Address address) {
        addressRepository.save(address);
    }


    @Override
    public void sendMarketingMessages(MarketingMessage marketingMessage) throws MessagingException {
        List<UserMarketingDetails> details = userRepository.findAllUserMarketingDetailsBy();
        for (UserMarketingDetails marketingDetails : details) {
            emailService.sendNewMarketingEmail(marketingDetails.getFirstName(), marketingDetails.getSecondName(), marketingMessage.getMessage(), marketingDetails.getEmail());
        }
    }


}
