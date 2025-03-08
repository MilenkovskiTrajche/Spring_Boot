package com.example.onlineshop.controller;

import com.example.onlineshop.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductsService productsService;
    private final NavBarService navBarService;

    public HomeController(ProductsService productsService,
                          NavBarService navBarService) {
        this.productsService = productsService;
        this.navBarService = navBarService;
    }

    @GetMapping({"/","/home"})
    public String home(Model model,
                       Authentication authentication) {
        model.addAttribute("products", productsService.findAll());
        navBarService.setupNavbar(model, authentication);
        return "index";
    }

}
