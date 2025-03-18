package com.example.onlineshop.controller;

import com.example.onlineshop.entity.Categories;
import com.example.onlineshop.entity.Products;
import com.example.onlineshop.service.CategoriesService;
import com.example.onlineshop.service.NavBarService;
import com.example.onlineshop.service.ProductsService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoriesController {
    private final CategoriesService categoriesService;
    private final ProductsService productsService;
    private final NavBarService navBarService;

    public CategoriesController(CategoriesService categoriesService,
                                ProductsService productsService,
                                NavBarService navBarService) {
        this.categoriesService = categoriesService;
        this.productsService = productsService;
        this.navBarService = navBarService;
    }

    @GetMapping("/{parent_category_Name}")
    public String showCategory(@PathVariable String parent_category_Name,
                               @RequestParam(required = false) String sort,
                               Model model,
                               Authentication authentication) {
        // Fetch category by name
        Categories category = categoriesService.findByName(parent_category_Name);
        if (category == null) {
            model.addAttribute("error", "Category not found");
        }
        // Fetch products by category ID
        assert category != null;
        List<Products> products = productsService.findByCategoryId(category.getId());
        sortProducts(sort, model, products);
        model.addAttribute("name", category.getName());
        model.addAttribute("sort", sort);
        navBarService.setupNavbar(model, authentication);
        return "search";
    }

    public void sortProducts(@RequestParam(required = false) String sort, Model model, List<Products> products) {
        if (sort != null) {
            switch (sort) {
                case "priceLowToHigh":
                    products.sort(Comparator.comparing(Products::getPrice));
                    break;
                case "priceHighToLow":
                    products.sort(Comparator.comparing(Products::getPrice).reversed());
                    break;
                case "name":
                    products.sort(Comparator.comparing(Products::getName));
                    break;
            }
        }
        model.addAttribute("products",products );
    }


    @GetMapping("/{parent_category_Name}/{subCategory_name}")
    public String showSubCategory(@PathVariable String parent_category_Name,
                                  @PathVariable String subCategory_name,
                                  @RequestParam(required = false) String sort,
                                  Model model,
                                  Authentication authentication) {
        Categories parentCategory = categoriesService.findByName(parent_category_Name);
        Categories category = categoriesService.findByNameAndParentCategoryId(subCategory_name, parentCategory.getId());
        List<Products> products = productsService.findAllByCategory_Id(category.getId());
        sortProducts(sort, model, products);
        model.addAttribute("name", parent_category_Name+"/"+subCategory_name);
        model.addAttribute("sort", sort);
        navBarService.setupNavbar(model, authentication);
        return "search";
    }
}
