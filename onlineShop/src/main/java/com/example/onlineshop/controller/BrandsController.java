package com.example.onlineshop.controller;

import com.example.onlineshop.entity.Role;
import com.example.onlineshop.entity.Users;
import com.example.onlineshop.repository.UsersRepository;
import com.example.onlineshop.service.NavBarService;
import com.example.onlineshop.service.ProductsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/brands")
public class BrandsController {

    private final ProductsService productsService;
    private final NavBarService navBarService;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;

    public BrandsController(ProductsService productsService,
                            NavBarService navBarService, PasswordEncoder passwordEncoder, PasswordEncoder passwordEncoder1, UsersRepository usersRepository) {
        this.productsService = productsService;
        this.navBarService = navBarService;
        this.passwordEncoder = passwordEncoder1;
        this.usersRepository = usersRepository;
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

        Users user = new Users("milenkovskitm",passwordEncoder.encode("m"),"m@gmai.com","Trajce","Milenkovski",Role.ADMIN);
        usersRepository.save(user);

        navBarService.setupNavbar(model,authentication);
        return "search"; // Make sure this matches the actual template name
    }
}
