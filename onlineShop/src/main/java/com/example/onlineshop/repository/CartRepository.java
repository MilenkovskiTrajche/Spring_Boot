package com.example.onlineshop.repository;

import com.example.onlineshop.entity.Cart;
import com.example.onlineshop.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Users userId);
}
