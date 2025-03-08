package com.example.onlineshop.service;

import com.example.onlineshop.entity.Role;
import com.example.onlineshop.entity.Users;
import com.example.onlineshop.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(String username, String password, String email, Role role, String name, String surname) {
        Users user = new Users(username, passwordEncoder.encode(password), email, name, surname, role);
        System.out.println("Saving user: " + user);  // Log user details before saving
        usersRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
