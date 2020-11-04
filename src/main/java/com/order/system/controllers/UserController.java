package com.order.system.controllers;

import com.order.system.exceptions.ExceptionHandling;
import com.order.system.exceptions.models.InvalidCredentialsException;
import com.order.system.exceptions.models.InvalidDataFormatException;
import com.order.system.exceptions.models.UserNotFoundException;
import com.order.system.models.Address;
import com.order.system.models.MarketingMessage;
import com.order.system.models.User;
import com.order.system.services.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.Collections;

import static com.order.system.constants.ErrorConstants.INVALID_DATA_FORMAT;
import static com.order.system.constants.ErrorConstants.NOT_ENOUGH_PERMISSION;
import static com.order.system.constants.GeneralConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api")
public class UserController extends ExceptionHandling {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user, BindingResult bindingResult) throws InvalidDataFormatException {
        if (bindingResult.hasErrors())
            throw new InvalidDataFormatException(INVALID_DATA_FORMAT);

        return ResponseEntity.ok(userService.saveUser(user));
    }

    @PutMapping("/updateAddress")
    public ResponseEntity<?> updateAddress(@RequestBody Address address) {
        userService.updateAddress(address);
        return ResponseEntity.ok("SUCCESS");
    }

    @PutMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) throws InvalidCredentialsException, UserNotFoundException {
        return ResponseEntity.ok(userService.login(user));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(TOKEN_PREFIX.length());

        userService.logOut(token);
        return ResponseEntity.ok("SUCCESS");
    }

    @PostMapping("/sendMarketingEmail")
    public ResponseEntity<?> sendMarketingEmail(@RequestBody MarketingMessage marketingMessage, HttpServletRequest request) throws AccessDeniedException, MessagingException {
        String token = request.getHeader("Authorization").substring(TOKEN_PREFIX.length());

        if (!userService.verifyUserPermissions(token, Collections.singletonList("ADMIN")))
            throw new AccessDeniedException(NOT_ENOUGH_PERMISSION);

        userService.sendMarketingMessages(marketingMessage);
        return ResponseEntity.ok("SUCCESSFULLY_SENT");
    }

}
