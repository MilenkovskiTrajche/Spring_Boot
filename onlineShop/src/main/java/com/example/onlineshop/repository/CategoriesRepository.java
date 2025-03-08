package com.example.onlineshop.repository;

import com.example.onlineshop.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    List<Categories> findByParentCategoryIsNull();
    Categories findByName(String name);
    Categories findByNameAndParentCategoryId(String name, long id);
}
