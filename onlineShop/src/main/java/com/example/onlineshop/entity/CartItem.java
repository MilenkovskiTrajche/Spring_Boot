package com.example.onlineshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products product;

    private String size;
    private Integer quantity;

    public CartItem(){}

    public CartItem(Cart cart, Products product, Integer quantity, String size) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.size = size;
    }

    // Add getters, setters, and constructors as needed
}
