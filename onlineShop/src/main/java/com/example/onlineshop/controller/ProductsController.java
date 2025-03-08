package com.example.onlineshop.controller;

import com.example.onlineshop.entity.Products;
import com.example.onlineshop.service.BrandsService;
import com.example.onlineshop.service.CategoriesService;
import com.example.onlineshop.service.NavBarService;
import com.example.onlineshop.service.ProductsService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/products")
public class ProductsController {
    private final ProductsService productsService;
    private final CategoriesService categoriesService;
    private final BrandsService brandsService;
    private final NavBarService navBarService;

    public ProductsController(ProductsService productsService,
                              CategoriesService categoriesService,
                              BrandsService brandsService,
                              NavBarService navBarService) {
        this.productsService = productsService;
        this.categoriesService = categoriesService;
        this.brandsService = brandsService;
        this.navBarService = navBarService;
    }


    @GetMapping("/viewProducts")
    public String showProducts(Model model) {
        model.addAttribute("products", productsService.findAll());
        return "products";
    }

    @GetMapping("/add")
    public String showProductForm(Model model) {
        model.addAttribute("categories", categoriesService.findTopLevelCategories()); // Fetch existing categories for dropdown
        model.addAttribute("product", new Products()); // Empty product for form binding
        model.addAttribute("brands", brandsService.findAll()); // Fetch existing categories for dropdown
        return "add_product";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute Products product,
                             @RequestParam("categoryId") Long categoryId,
                             @RequestParam("brandId") Long brandId,
                             @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        productsService.saveProduct(product, categoryId,brandId, imageFile);
        return "redirect:/";
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

    // Show Add/Edit Category Form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               Model model){
        Products product = productsService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoriesService.findTopLevelCategories());
        model.addAttribute("brands", brandsService.findAll());
        return "add_product"; // Reuse same form for adding and editing
    }

    @PostMapping("/edit/{id}")
    public String editCategory(@ModelAttribute Products products,
                               @RequestParam("categoryId") Long categoryId,
                               @RequestParam("brandId") Long brandId,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               @RequestParam(value = "keepImage", defaultValue = "false") boolean keepImage) throws Exception {
        productsService.updateProduct(products, categoryId, brandId, imageFile, keepImage);
        return "redirect:/products/viewProducts";
    }

    // Delete Category
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        productsService.deleted_byId(id);
        return "redirect:/products/viewProducts";
    }
}
