package com.example.onlineshop.controller;

import com.example.onlineshop.entity.Products;
import com.example.onlineshop.service.NavBarService;
import com.example.onlineshop.service.ProductsService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductsController {
    private final ProductsService productsService;
    private final NavBarService navBarService;

    public ProductsController(ProductsService productsService,
                              NavBarService navBarService) {
        this.productsService = productsService;
        this.navBarService = navBarService;
    }

    @GetMapping("/{category_name}/{subcategory_name}/{product_Id}/{product_name}")
    public String showProductOverview(@PathVariable Long product_Id,
                                      @PathVariable String category_name,
                                      @PathVariable String product_name,
                                      @PathVariable String subcategory_name,
                                      Model model,
                                      Authentication authentication) {
        Products product = productsService.findById(product_Id);
        model.addAttribute("product", product);
        navBarService.setupNavbar(model,authentication);
        return "product_overview";
    }


}
