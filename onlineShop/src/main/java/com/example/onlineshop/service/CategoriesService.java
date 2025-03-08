package com.example.onlineshop.service;

import com.example.onlineshop.entity.Categories;
import com.example.onlineshop.repository.CategoriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public List<Categories> findAll() {
        return categoriesRepository.findAll();
    }

    public Categories findById(Long id) {
        return categoriesRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void saveCategory(Categories category, Long parentId) {
        if (parentId != null) {
            Categories parentCategory = findById(parentId);
            category.setParentCategory(parentCategory);
        } else {
            category.setParentCategory(null);
        }
        categoriesRepository.save(category);
    }

    public void updateCategory(Long id, Categories newCategory, Long parentId) {
        Categories existingCategory = findById(id);
        existingCategory.setName(newCategory.getName());

        if (parentId != null) {
            existingCategory.setParentCategory(findById(parentId));
        } else {
            existingCategory.setParentCategory(null);
        }

        categoriesRepository.save(existingCategory);
    }

    public void deleteCategory(Long id) {
        categoriesRepository.deleteById(id);
    }

    public List<Categories> findTopLevelCategories() {
        return categoriesRepository.findByParentCategoryIsNull(); // Fetches only top-level categories
    }

    public Categories findByName(String name){
        return categoriesRepository.findByName(name);
    }

    public Categories findByNameAndParentCategoryId(String name, Long parentCategoryId) {
        return categoriesRepository.findByNameAndParentCategoryId(name, parentCategoryId);
    }

    public List<Categories> findByParentCategoryIsNull() {
        return categoriesRepository.findByParentCategoryIsNull();
    }

}
