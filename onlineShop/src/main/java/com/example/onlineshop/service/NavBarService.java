package com.example.onlineshop.service;

import com.example.onlineshop.entity.Cart;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class NavBarService {
    private final CategoriesService categoriesService;
    private final BrandsService brandsService;
    private final CartService cartService;

    public NavBarService(CategoriesService categoriesService,
                         BrandsService brandsService,
                         CartService cartService) {
        this.categoriesService = categoriesService;
        this.brandsService = brandsService;
        this.cartService = cartService;
    }

    public void setupNavbar(Model model,
                            Authentication authentication) {
        // Handle authentication details
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);

            boolean isAdmin = authentication.getAuthorities()
                    .stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("isAdmin", isAdmin);

            // Check if the user has items in their cart
            Cart cart = cartService.getCartByUsername(username);
            model.addAttribute("cartItemCount", cart != null ? cart.getItems().size() : 0);
        } else {
            model.addAttribute("userStatus", "none");
        }

        // Add categories and brands to the model
        model.addAttribute("categories", categoriesService.findTopLevelCategories());
        model.addAttribute("brands", brandsService.findAll());
    }
}
