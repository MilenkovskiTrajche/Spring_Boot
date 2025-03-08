package com.example.onlineshop.controller;

import com.example.onlineshop.entity.Role;
import com.example.onlineshop.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {

    private final UsersService usersService;

    public RegisterController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/new/user/registration")
    public String register(@RequestParam String name,
                           @RequestParam String surname,
                           @RequestParam String email,
                           @RequestParam String username,
                           @RequestParam String password) {
        usersService.registerUser(username, password, email, Role.USER, name, surname);
        return "redirect:/login";  // Redirect after successful registration
    }


    @GetMapping("/new/user/registration")
    public String registrationForm() {
        return "register";  // Return the registration form view
    }


}
