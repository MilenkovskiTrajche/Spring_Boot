package com.example.onlineshop.controller;

import com.example.onlineshop.entity.Cart;
import com.example.onlineshop.entity.CartItem;
import com.example.onlineshop.entity.Products;
import com.example.onlineshop.repository.CartItemRepository;
import com.example.onlineshop.repository.CartRepository;
import com.example.onlineshop.service.CartService;
import com.example.onlineshop.service.NavBarService;
import com.example.onlineshop.service.ProductsService;
import com.example.onlineshop.service.UsersService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final NavBarService navBarService;
    private final CartService cartService;
    private final UsersService usersService;
    private final ProductsService productsService;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    public CartController(NavBarService navBarService,
                          CartService cartService,
                          UsersService usersService, ProductsService productsService, CartItemRepository cartItemRepository, CartRepository cartRepository) {
        this.navBarService = navBarService;
        this.cartService = cartService;
        this.usersService = usersService;
        this.productsService = productsService;
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
    }

    @GetMapping
    public String viewCart(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();

            // Fetch the cart for the logged-in user
            Cart cart = cartService.getCartByUsername(username);

            // If no cart exists, create a new cart for the user
            if (cart == null) {
                cart = new Cart();
                cart.setUserId(usersService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found")));
                cartService.save(cart);  // Save the newly created cart to the database
            }

            // Check if the cart has items
            if (cart.getItems() == null || cart.getItems().isEmpty()) {
                model.addAttribute("cartEmpty", true);  // Cart is empty
            } else {
                model.addAttribute("cartItems", cart.getItems());
                model.addAttribute("cartEmpty", false);  // Cart has items
            }

            model.addAttribute("cart", cart);
            model.addAttribute("totalPrice", cartService.calculateTotalPrice(cart));
            navBarService.setupNavbar(model, authentication);

            return "cart"; // Return to the cart view
        }
        return "redirect:/login"; // Redirect to login if not authenticated
    }


    @PostMapping("/add")
    public String addtoCart(@RequestParam("productId") Long productId,
                            @RequestParam("size") String size,
                            @RequestParam("quantity") Integer quantity,
                            Authentication authentication,
                            Model model) {

        String username;
        if (authentication != null && authentication.isAuthenticated()) {
            username = authentication.getName();
        } else {
            return "redirect:/login";
        }

        // Ensure a cart exists for the user
        Cart cart = cartService.getCartByUsername(username);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(usersService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found")));
            cartService.save(cart);  // Save the new cart
        }

        // Fetch the product using the productId
        Products product = productsService.getProductById(productId);
        if (product == null) {
            return "redirect:/cart"; // Redirect if the product is not found
        }

        // Create a new CartItem using the product, size, and quantity
        CartItem cartItem = new CartItem(cart, product, quantity, size);

        // Add the CartItem to the cart (you might have a method in Cart to add an item)
        cartService.addItemToCart(cart, cartItem); // Implement this method in your CartService if necessary

        // Fetch the updated cart
        cart = cartService.getCartByUsername(username);

        // Add the updated cart and total price to the model
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cartService.calculateTotalPrice(cart));

        // Update the navbar
        navBarService.setupNavbar(model, authentication);

        // Redirect to the cart page to show updated cart
        return "redirect:/cart";
    }


    @PostMapping("/delete/{cartId}")
    public String deleteFromCart(@PathVariable Long cartId,
                                 @RequestParam("cartItemId") Long cartItemId,
                                 Authentication authentication,
                                 Model model) {

        Cart cart = cartService.getCartById(cartId);
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        cartItem.ifPresent(cartItemRepository::delete);

        cartRepository.save(cart);

        // Add the cart and total price to the model
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cartService.calculateTotalPrice(cart));

        // Update the navbar
        navBarService.setupNavbar(model, authentication);

        return "redirect:/cart"; // Return to the cart view
    }
}
