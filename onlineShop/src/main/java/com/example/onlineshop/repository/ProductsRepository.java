package com.example.onlineshop.repository;

import com.example.onlineshop.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
    List<Products> findAllByDescriptionContainingOrNameContaining(String description, String name);
    List<Products> findAllByCategory_Id(long id);
    List<Products> findAllByCategory_ParentCategoryId(Long category_Id);
    List<Products> findAllByBrand_Name(String brand_name);

}
