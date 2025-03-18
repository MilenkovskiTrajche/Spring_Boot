package com.example.onlineshop.controller;

import com.example.onlineshop.entity.*;
import com.example.onlineshop.service.BrandsService;
import com.example.onlineshop.service.CategoriesService;
import com.example.onlineshop.service.ProductsService;
import com.example.onlineshop.service.UsersService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final BrandsService brandsService;
    private final CategoriesService categoriesService;
    private final ProductsService productsService;
    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    public AdminController(BrandsService brandsService,
                           CategoriesService categoriesService,
                           ProductsService productsService,
                           UsersService usersService,
                           PasswordEncoder passwordEncoder) {
        this.brandsService = brandsService;
        this.categoriesService = categoriesService;
        this.productsService = productsService;
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("")
    public String Admin() {
        return "admin";
    }

    //brand add
    @GetMapping("/brands/add")
    public String showBrandsForm(Model model) {
        model.addAttribute("brand", new Brands()); // Empty category for form binding
        return "add_brands";
    }

    @PostMapping("/brands/add")
    public String addBrand(@RequestParam String name) {
        Brands brands = new Brands(name);
        brandsService.save(brands);
        return "redirect:/admin/brands/viewBrands";
    }

    // Show Add/Edit Category Form
    @GetMapping("/brands/edit/{id}")
    public String showEditBrandsForm(@PathVariable Long id, Model model) {
        model.addAttribute("brand", brandsService.findById(id));
        model.addAttribute("brands", brandsService.findAll()); // Fetch main categories for dropdown
        return "add_brands"; // Reuse same form for adding and editing
    }

    // Handle Edit Category Form Submission
    @PostMapping("/brands/edit/{id}")
    public String editBrand(@PathVariable Long id,
                               @ModelAttribute Brands brands) {
        brandsService.updateBrand(id, brands);
        return "redirect:/admin/brands/viewBrands";
    }

    // Delete Category
    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable Long id) {
        brandsService.deleteById(id);
        return "redirect:/admin/brands/viewBrands";
    }

    @GetMapping("/brands/viewBrands")
    public String brands(Model model) {
        model.addAttribute("brands", brandsService.findAll()); // Fetch existing categories for dropdown
        return "brands";
    }

    //categories add/edit/delete
    @GetMapping("/categories/viewCategories")
    public String categories(Model model) {
        model.addAttribute("categories", categoriesService.findAll()); // Fetch existing categories for dropdown
        return "categories";
    }
    @GetMapping("/categories/add")
    public String showCategoryForm(Model model) {
        model.addAttribute("categories", categoriesService.findByParentCategoryIsNull()); // Fetch existing categories for dropdown
        model.addAttribute("category", new Categories()); // Empty category for form binding
        return "add_category";
    }

    @PostMapping("/categories/add")
    public String addCategory(@ModelAttribute Categories category,
                              @RequestParam(required = false) Long parentId) {
        categoriesService.saveCategory(category, parentId);
        return "redirect:/admin/categories/viewCategories";
    }

    // Show Add/Edit Category Form
    @GetMapping("/categories/edit/{id}")
    public String showEditCategoryForm(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoriesService.findById(id));
        model.addAttribute("categories", categoriesService.findAll()); // Fetch main categories for dropdown
        return "add_category"; // Reuse same form for adding and editing
    }

    // Handle Edit Category Form Submission
    @PostMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable Long id,
                               @ModelAttribute Categories category,
                               @RequestParam(required = false) Long parentId) {
        categoriesService.updateCategory(id, category, parentId);
        return "redirect:/admin/categories/viewCategories";
    }

    // Delete Category
    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoriesService.deleteCategory(id);
        return "redirect:/admin/categories/viewCategories";
    }

    //products add/edit/delete
    @GetMapping("/products/viewProducts")
    public String showProducts(Model model) {
        model.addAttribute("products", productsService.findAll());
        return "products";
    }

    @GetMapping("/products/add")
    public String showProductForm(Model model) {
        model.addAttribute("categories", categoriesService.findTopLevelCategories()); // Fetch existing categories for dropdown
        model.addAttribute("product", new Products()); // Empty product for form binding
        model.addAttribute("brands", brandsService.findAll()); // Fetch existing categories for dropdown
        return "add_product";
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute Products product,
                             @RequestParam("categoryId") Long categoryId,
                             @RequestParam("brandId") Long brandId,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             @RequestParam(value = "availableSizes", required = false) List<String> availableSizes) throws IOException {
        productsService.saveProduct(product, categoryId,brandId, imageFile,availableSizes);
        return "redirect:/admin/products/viewProducts";
    }

    // Show Add/Edit Category Form
    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               Model model){
        model.addAttribute("product", productsService.findById(id));
        model.addAttribute("categories", categoriesService.findTopLevelCategories());
        model.addAttribute("brands", brandsService.findAll());
        return "add_product"; // Reuse same form for adding and editing
    }

    @PostMapping("/products/edit/{id}")
    public String editCategory(@ModelAttribute Products products,
                               @RequestParam("categoryId") Long categoryId,
                               @RequestParam("brandId") Long brandId,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               @RequestParam(value = "keepImage", defaultValue = "false") boolean keepImage,
                               @RequestParam(value = "availableSizes", required = false) List<String> availableSizes) throws Exception {
        productsService.updateProduct(products, categoryId, brandId, imageFile, keepImage,availableSizes);
        return "redirect:/admin/products/viewProducts";
    }

    // Delete Category
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productsService.deleted_byId(id);
        return "redirect:/admin/products/viewProducts";
    }


    //users add/edit/delete
    @GetMapping("/users/viewUsers")
    public String showUsers(Model model) {
        model.addAttribute("users", usersService.findAll());
        return "users";
    }

    @GetMapping("/users/add")
    public String showUserForm(Model model) {
        model.addAttribute("user", new Users()); // Empty product for form binding
        model.addAttribute("roles", Role.values());  // Pass all roles to the form
        return "add_users";
    }

    @PostMapping("/users/add")
    public String addUser(@ModelAttribute Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersService.save(user);
        return "redirect:/admin/users/viewUsers";
    }

    // Show Add/Edit Category Form
    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id,
                               Model model){
        model.addAttribute("user", usersService.findById(id));
        model.addAttribute("roles", Role.values());  // Pass all roles to the form
        return "add_users"; // Reuse same form for adding and editing
    }

    @PostMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id,
                           @ModelAttribute Users user) throws Exception {
        // Encrypt the password only if it's provided
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }else {
            Users user1 = usersService.findById(id);
            user.setPassword(user1.getPassword());
        }
        usersService.updateUser(id, user);
        return "redirect:/admin/users/viewUsers";
    }

    // Delete Category
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        usersService.deleted_byId(id);
        return "redirect:/admin/users/viewUsers";
    }
}
