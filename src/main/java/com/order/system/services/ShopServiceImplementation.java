package com.order.system.services;

import com.order.system.dto.UserDto;
import com.order.system.dtoInterfaces.UserDetails;
import com.order.system.exceptions.models.*;
import com.order.system.models.*;
import com.order.system.repositories.*;
import com.order.system.services.interfaces.ShopService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
@Transactional
public class ShopServiceImplementation implements ShopService {

    private final MenuRepository menuRepository;
    private final MenuItemsRepository menuItemsRepository;
    private final ItemsToBuyRepository itemsToBuyRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public ShopServiceImplementation(MenuRepository menuRepository, MenuItemsRepository menuItemsRepository, ItemsToBuyRepository itemsToBuyRepository, CartRepository cartRepository, UserRepository userRepository, AddressRepository addressRepository) {
        this.menuRepository = menuRepository;
        this.menuItemsRepository = menuItemsRepository;
        this.itemsToBuyRepository = itemsToBuyRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public Menu createMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public MenuItems addItemInMenu(Integer menuId, MenuItems item) throws MenuNotExistException {

        if (!menuRepository.existsById(menuId))
            throw new MenuNotExistException();

        item.setMenuId(menuId);
        return menuItemsRepository.save(item);
    }

    @Override
    public Menu viewMenu(Integer menuId) throws MenuNotExistException {

        if (!menuRepository.existsById(menuId))
            throw new MenuNotExistException();

        return menuRepository.findById(menuId).orElse(null);
    }

    @Override
    public void addItemsToCart(ItemsToBuy itemsToBuy) throws CartNotFoundException, ItemExistInTheCartException {
        if (!cartRepository.existsById(itemsToBuy.getCartId()))
            throw new CartNotFoundException();

        if (itemsToBuyRepository.existsByItemId(itemsToBuy.getItemId()))
            throw new ItemExistInTheCartException();

        if (!menuItemsRepository.existsById(itemsToBuy.getItemId()))


            itemsToBuyRepository.save(itemsToBuy);
    }

    @Override
    public List<Menu> viewAllMenus() {
        return menuRepository.findAll();
    }

    @Override
    public Cart viewCart(String token) {
        User dbUser = userRepository.findByToken(token);
        Cart userCart = dbUser.getCart();
        if (userCart.getItemsToBuy().size() == 0) {
            userCart.setTotal(0.0);
            return userCart;
        }

        userCart.setItemsToDisplay(getItemsToDisplay(userCart.getItemsToBuy()));
        userCart.setTotal(calculateCardTotal(userCart.getItemsToDisplay()));
        return userCart;
    }

    @Override
    public void deleteItemFromMenu(Integer itemId) throws ItemNotExitIntoMenuException {
        if (!menuItemsRepository.existsById(itemId))
            throw new ItemNotExitIntoMenuException();

        menuItemsRepository.deleteById(itemId);
    }

    @Override
    public void deleteItemFromCart(Integer itemId) throws ItemNotExistInTheCartException {
        if (!itemsToBuyRepository.existsById(itemId))
            throw new ItemNotExistInTheCartException();

        itemsToBuyRepository.deleteById(itemId);
    }

    @Override
    public void completeOrder(CompleteOrder completeOrder, String token) throws IncorrectAmountToPayException {
        User currentUser = userRepository.findByToken(token);
        Cart currentUserCart = viewCart(token);

        if (!completeOrder.getTotalToPay().equals(currentUserCart.getTotal()))
            throw new IncorrectAmountToPayException();

        String orderNumber = "OR" + RandomStringUtils.randomNumeric(5);
        currentUser.setCurrentNumber(orderNumber);
        currentUserCart.getItemsToBuy().clear();
        currentUserCart.getItemsToDisplay().clear();
        currentUserCart.setTotal(0.0);

        currentUser.setCart(currentUserCart);
        userRepository.save(currentUser);
    }

    @Override
    public UserDto viewUserDetails(String orderId) throws OrderNotExistException {
        if (!userRepository.existsByCurrentNumber(orderId))
            throw new OrderNotExistException();

        UserDetails userDetails = userRepository.findByCurrentNumber(orderId);

        UserDto userDto = new UserDto();
        userDto.setFullName(userDetails.getFirstName() + " " + userDetails.getSecondName());
        userDto.setPhone(userDetails.getPhone());
        userDto.setOrderNumber(userDetails.getCurrentNumber());
        userDto.setAddressUserDetails(addressRepository.findAddressUserDetailsById(userDetails.getId()));

        return userDto;
    }

    @Override
    public void deliveryCompleted(String token) {
        userRepository.updateOrderNumber(StringUtils.EMPTY, token);
    }

    private List<MenuItems> getItemsToDisplay(Set<ItemsToBuy> itemsToBuys) {
        List<MenuItems> itemsToReturn = new ArrayList<>();

        for (ItemsToBuy it : itemsToBuys) {
            MenuItems item = menuItemsRepository.findById(it.getItemId()).orElse(null);
            if (item == null) continue;

            item.setQuantity(it.getQuantity());
            itemsToReturn.add(item);
        }

        return itemsToReturn;
    }

    private Double calculateCardTotal(List<MenuItems> menuItems) {
        return menuItems.stream().mapToDouble(MenuItems::getPrice).sum();
    }
}
