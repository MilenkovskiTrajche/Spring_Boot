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

    @GetMapping("/viewCategories")
    public String categories(Model model) {
        model.addAttribute("categories", categoriesService.findAll()); // Fetch existing categories for dropdown
        return "categories";
    }
    @GetMapping("/add")
    public String showCategoryForm(Model model) {
        model.addAttribute("categories", categoriesService.findByParentCategoryIsNull()); // Fetch existing categories for dropdown
        model.addAttribute("category", new Categories()); // Empty category for form binding
        return "add_category";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute Categories category,
                              @RequestParam(required = false) Long parentId) {
        categoriesService.saveCategory(category, parentId);
        return "redirect:/categories/viewCategories";
    }

    // Show Add/Edit Category Form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoriesService.findById(id));
        model.addAttribute("categories", categoriesService.findAll()); // Fetch main categories for dropdown
        return "add_category"; // Reuse same form for adding and editing
    }

    // Handle Edit Category Form Submission
    @PostMapping("/edit/{id}")
    public String editCategory(@PathVariable Long id,
                               @ModelAttribute Categories category,
                               @RequestParam(required = false) Long parentId) {
        categoriesService.updateCategory(id, category, parentId);
        return "redirect:/categories/viewCategories";
    }

    // Delete Category
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoriesService.deleteCategory(id);
        return "redirect:/categories/viewCategories";
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
