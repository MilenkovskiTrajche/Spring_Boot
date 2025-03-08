package com.example.onlineshop.controller;

import com.example.onlineshop.service.NavBarService;
import com.example.onlineshop.service.ProductsService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
    private final ProductsService productsService;
    private final NavBarService navBarService;

    public SearchController(ProductsService productsService,
                            NavBarService navBarService) {
        this.productsService = productsService;
        this.navBarService = navBarService;
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "product_name") String search_product,
                         Model model,
                         Authentication authentication) {
        if (!search_product.isEmpty()) {
            model.addAttribute("products", productsService.findByCategoryOrNameOrDescription(search_product));
            model.addAttribute("name", search_product);
            navBarService.setupNavbar(model, authentication);
            return "search";
        }else{
            return "redirect:/";
        }
    }

}
