package com.example.onlineshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "products")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Float price;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "category_id") // Foreign key reference
    private Categories category;

    @ManyToOne
    @JoinColumn(name = "brand_id") // Foreign key reference
    private Brands brand;

    private String image;

    @ElementCollection
    @CollectionTable(name = "product_sizes", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "size")
    private List<String> availableSizes;

    public Products() {}

    public Products(String name, String description, Float price, Integer quantity, Categories category, String image, Brands brand,List<String> availableSizes) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.image = image;
        this.brand = brand;
        this.availableSizes = availableSizes;
    }

}
