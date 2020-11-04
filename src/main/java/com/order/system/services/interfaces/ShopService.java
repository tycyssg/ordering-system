package com.order.system.services.interfaces;

import com.order.system.dto.UserDto;
import com.order.system.exceptions.models.*;
import com.order.system.models.*;

import java.util.List;

public interface ShopService {

    public Menu createMenu(Menu menu);

    public MenuItems addItemInMenu(Integer menuId, MenuItems item) throws MenuNotExistException;

    public Menu viewMenu(Integer menuId) throws MenuNotExistException;

    public void addItemsToCart(ItemsToBuy itemsToBuy) throws CartNotFoundException, ItemExistInTheCartException;

    public List<Menu> viewAllMenus();

    public Cart viewCart(String token);

    void deleteItemFromMenu(Integer itemId) throws ItemNotExitIntoMenuException;

    void deleteItemFromCart(Integer itemId) throws ItemNotExistInTheCartException;

    void completeOrder(CompleteOrder completeOrder, String token) throws IncorrectAmountToPayException;

    UserDto viewUserDetails(String orderId) throws OrderNotExistException;

    void deliveryCompleted(String token);
}
