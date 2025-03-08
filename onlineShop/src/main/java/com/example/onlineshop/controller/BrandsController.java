package com.example.onlineshop.controller;

import com.example.onlineshop.entity.Brands;
import com.example.onlineshop.service.BrandsService;
import com.example.onlineshop.service.NavBarService;
import com.example.onlineshop.service.ProductsService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/brands")
public class BrandsController {

    private final BrandsService brandsService;
    private final ProductsService productsService;
    private final NavBarService navBarService;
    public BrandsController(BrandsService brandsService,
                            ProductsService productsService,
                            NavBarService navBarService) {
        this.brandsService = brandsService;
        this.productsService = productsService;
        this.navBarService = navBarService;
    }

    @GetMapping("/add")
    public String showBrandsForm() {
        return "add_brands";
    }

    @PostMapping("/add")
    public String addBrand(@RequestParam String name) {
        Brands brands = new Brands(name);
        brandsService.save(brands);
        return "redirect:/";
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
