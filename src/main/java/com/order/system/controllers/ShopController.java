package com.order.system.controllers;

import com.order.system.dto.UserDto;
import com.order.system.exceptions.ExceptionHandling;
import com.order.system.exceptions.models.*;
import com.order.system.models.*;
import com.order.system.services.interfaces.ShopService;
import com.order.system.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.order.system.constants.ErrorConstants.INVALID_DATA_FORMAT;
import static com.order.system.constants.ErrorConstants.NOT_ENOUGH_PERMISSION;
import static com.order.system.constants.GeneralConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api")
public class ShopController extends ExceptionHandling {

    private final UserService userService;
    private final ShopService shopService;

    @Autowired
    public ShopController(ShopService shopService, UserService userService) {
        this.shopService = shopService;
        this.userService = userService;
    }

    @PostMapping("/createMenu")
    public ResponseEntity<Menu> createMenu(@RequestBody Menu menu, HttpServletRequest request) throws AccessDeniedException {
        String token = request.getHeader("Authorization").substring(TOKEN_PREFIX.length());

        if (!userService.verifyUserPermissions(token, Collections.singletonList("ADMIN")))
            throw new AccessDeniedException(NOT_ENOUGH_PERMISSION);

        return ResponseEntity.ok(shopService.createMenu(menu));
    }

    @PostMapping("/addItemToMenu")
    public ResponseEntity<MenuItems> addItemToMenu(@RequestParam Integer menuId, @RequestBody MenuItems menuItem, HttpServletRequest request) throws AccessDeniedException, MenuNotExistException {
        String token = request.getHeader("Authorization").substring(TOKEN_PREFIX.length());

        if (!userService.verifyUserPermissions(token, Collections.singletonList("ADMIN")))
            throw new AccessDeniedException(NOT_ENOUGH_PERMISSION);

        return ResponseEntity.ok(shopService.addItemInMenu(menuId, menuItem));
    }

    @DeleteMapping("/deleteItemFromMenu/{itemId}")
    public ResponseEntity<?> deleteItemFromMenu(@PathVariable("itemId") Integer itemId, HttpServletRequest request) throws AccessDeniedException, MenuNotExistException, ItemNotExitIntoMenuException {
        String token = request.getHeader("Authorization").substring(TOKEN_PREFIX.length());

        if (!userService.verifyUserPermissions(token, Collections.singletonList("ADMIN")))
            throw new AccessDeniedException(NOT_ENOUGH_PERMISSION);
        shopService.deleteItemFromMenu(itemId);

        return ResponseEntity.ok("DELETED");
    }

    @GetMapping("/viewMenu")
    public ResponseEntity<Menu> viewMenu(@RequestParam Integer menuId) throws MenuNotExistException {
        return ResponseEntity.ok(shopService.viewMenu(menuId));
    }

    @GetMapping("/viewAllMenus")
    public ResponseEntity<List<Menu>> viewAllMenus() {
        return ResponseEntity.ok(shopService.viewAllMenus());
    }


    @GetMapping("/viewUserDetails")
    public ResponseEntity<UserDto> viewUserDetails(@RequestParam String orderId, HttpServletRequest request) throws AccessDeniedException, OrderNotExistException {
        String token = request.getHeader("Authorization").substring(TOKEN_PREFIX.length());

        if (!userService.verifyUserPermissions(token, Arrays.asList("ADMIN", "DRIVER")))
            throw new AccessDeniedException(NOT_ENOUGH_PERMISSION);

        return ResponseEntity.ok(shopService.viewUserDetails(orderId));
    }

    @PostMapping("/addItemToCart")
    public ResponseEntity<?> addItemToCart(@RequestBody ItemsToBuy itemsToBuy) throws CartNotFoundException, ItemExistInTheCartException {
        shopService.addItemsToCart(itemsToBuy);
        return ResponseEntity.ok("ITEM_ADDED");
    }

    @DeleteMapping("/deleteItemFromCart/{itemId}")
    public ResponseEntity<?> deleteItemFromCart(@PathVariable("itemId") Integer itemId) throws ItemNotExistInTheCartException {
        shopService.deleteItemFromCart(itemId);
        return ResponseEntity.ok("DELETED");
    }

    @GetMapping("/viewCart")
    public ResponseEntity<Cart> viewCart(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(TOKEN_PREFIX.length());

        return ResponseEntity.ok(shopService.viewCart(token));
    }


    @PostMapping("/completeOrder")
    public ResponseEntity<?> completeOrder(@Valid @RequestBody CompleteOrder completeOrder, BindingResult bindingResult, HttpServletRequest request) throws InvalidDataFormatException, IncorrectAmountToPayException {

        if (bindingResult.hasErrors())
            throw new InvalidDataFormatException(INVALID_DATA_FORMAT);

        String token = request.getHeader("Authorization").substring(TOKEN_PREFIX.length());

        shopService.completeOrder(completeOrder, token);
        return ResponseEntity.ok("ORDER_COMPLETE");
    }


    @GetMapping("/deliveryCompleted")
    public ResponseEntity<?> deliveryCompleted(HttpServletRequest request) throws AccessDeniedException {
        String token = request.getHeader("Authorization").substring(TOKEN_PREFIX.length());

        if (!userService.verifyUserPermissions(token, Arrays.asList("ADMIN", "DRIVER")))
            throw new AccessDeniedException(NOT_ENOUGH_PERMISSION);

        shopService.deliveryCompleted(token);

        return ResponseEntity.ok("SUCCESS");
    }

}
