package com.example.onlineshop.controller;

import com.example.onlineshop.entity.Categories;
import com.example.onlineshop.service.CategoriesService;
import com.example.onlineshop.service.NavBarService;
import com.example.onlineshop.service.ProductsService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                               Model model,
                               Authentication authentication) {
        // Fetch category by name
        Categories category = categoriesService.findByName(parent_category_Name);
        if (category == null) {
            model.addAttribute("error", "Category not found");
        }
        // Fetch products by category ID
        assert category != null;
        model.addAttribute("products", productsService.findByCategoryId(category.getId()));
        model.addAttribute("name", category.getName());
        navBarService.setupNavbar(model, authentication);
        return "search"; // Make sure this matches the actual template name
    }


    @GetMapping("/{parent_category_Name}/{subCategory_name}")
    public String showSubCategory(@PathVariable String parent_category_Name,
                                  @PathVariable String subCategory_name,
                                  Model model,
                                  Authentication authentication) {
        Categories parentCategory = categoriesService.findByName(parent_category_Name);
        Categories category = categoriesService.findByNameAndParentCategoryId(subCategory_name, parentCategory.getId());
        model.addAttribute("products", productsService.findAllByCategory_Id(category.getId()));
        model.addAttribute("name", parent_category_Name+"/"+subCategory_name);
        navBarService.setupNavbar(model, authentication);
        return "search";
    }
}
