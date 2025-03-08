package com.example.onlineshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "categories")
public class Categories {
    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    // Self-referencing Many-to-One relationship for parent category
    @ManyToOne
    @JoinColumn(name = "parent_id") // References parent category ID
    private Categories parentCategory;

    // One-to-Many relationship for child categories
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<Categories> subCategories;

}
