package com.example.onlineshop.controller;

import com.example.onlineshop.service.NavBarService;
import com.example.onlineshop.service.ProductsService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/brands")
public class BrandsController {

    private final ProductsService productsService;
    private final NavBarService navBarService;

    public BrandsController(ProductsService productsService,
                            NavBarService navBarService) {
        this.productsService = productsService;
        this.navBarService = navBarService;
    }


    @GetMapping("/{brand_name}")
    public String showBrands(@PathVariable String brand_name,
                               Model model,Authentication authentication) {
        if(brand_name.equals("all")) {
            model.addAttribute("products", productsService.findAll());
            model.addAttribute("name", "ALL BRANDS");
        }else {
            model.addAttribute("products", productsService.findAllByBrand_Name(brand_name));
            model.addAttribute("name", brand_name);
        }
        navBarService.setupNavbar(model,authentication);
        return "search"; // Make sure this matches the actual template name
    }
}
