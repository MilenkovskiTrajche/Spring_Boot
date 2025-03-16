package com.example.onlineshop.service;

import com.example.onlineshop.entity.Cart;
import com.example.onlineshop.entity.CartItem;
import com.example.onlineshop.entity.Users;
import com.example.onlineshop.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UsersService usersService;

    public CartService(CartRepository cartRepository,
                       UsersService usersService) {
        this.cartRepository = cartRepository;
        this.usersService = usersService;
    }

    public Cart getCartByUsername(String username) {
        Users user = usersService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepository.findByUserId(user);
    }

    public double calculateTotalPrice(Cart cart) {
        double totalPrice = 0.0;
        for (CartItem cartItem : cart.getItems()) {
            totalPrice += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }
        return totalPrice;
    }

    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    public void addItemToCart(Cart cart, CartItem cartItem) {
        cart.getItems().add(cartItem);
        cartRepository.save(cart); // Save the updated cart
    }


    public Cart getCartById(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> new RuntimeException("Cart not found"));
    }

}
