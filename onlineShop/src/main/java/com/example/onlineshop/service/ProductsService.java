package com.example.onlineshop.service;

import com.example.onlineshop.entity.Brands;
import com.example.onlineshop.entity.Categories;
import com.example.onlineshop.entity.Products;
import com.example.onlineshop.repository.BrandsRepository;
import com.example.onlineshop.repository.CategoriesRepository;
import com.example.onlineshop.repository.ProductsRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ProductsService {
    private final ProductsRepository productsRepository;
    private final CategoriesRepository categoriesRepository;
    private final BrandsRepository brandsRepository;

    private static final String IMAGE_UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    public ProductsService(ProductsRepository productsRepository,
                           CategoriesRepository categoriesRepository,
                           BrandsRepository brandsRepository) {
        this.productsRepository = productsRepository;
        this.categoriesRepository = categoriesRepository;
        this.brandsRepository = brandsRepository;
    }

    public void saveProduct(Products product, Long categoryId, Long brandId, MultipartFile imageFile) throws IOException {
        Optional<Categories> category = categoriesRepository.findById(categoryId);
        category.ifPresent(product::setCategory);

        Optional<Brands> brand = brandsRepository.findById(brandId);
        brand.ifPresent(product::setBrand);

        String imagePath = IMAGE_UPLOAD_DIR + imageFile.getOriginalFilename();
        Files.write(Paths.get(imagePath), imageFile.getBytes());
        product.setImage("/uploads/" + imageFile.getOriginalFilename());


        productsRepository.save(product);
    }


    // Fetch all products
    public List<Products> findAll() {
        return productsRepository.findAll();
    }

    public List<Products> findByCategoryOrNameOrDescription(String name) {
        return productsRepository.findAllByDescriptionContainingOrNameContaining(name,name);
    }

    public List<Products> findByCategoryId(Long categoryId) {
        return productsRepository.findAllByCategory_ParentCategoryId(categoryId);
    }

    public void updateProduct(Products product, Long categoryId, Long brandId,
                              MultipartFile imageFile, boolean keepImage) throws Exception {
        Products existingProduct = productsRepository.findById(product.getId())
                .orElseThrow(Exception::new);

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setCategory(categoriesRepository.findById(categoryId).orElse(null));
        existingProduct.setBrand(brandsRepository.findById(brandId).orElse(null));

        if (!keepImage && !imageFile.isEmpty()) { // Only update if checkbox is unchecked & new image provided
            String imagePath = IMAGE_UPLOAD_DIR + imageFile.getOriginalFilename();
            Files.write(Paths.get(imagePath), imageFile.getBytes());
            existingProduct.setImage("/uploads/" + imageFile.getOriginalFilename());
        }

        productsRepository.save(existingProduct);
    }


    public Products findById(Long id) {
        return productsRepository.findById(id).orElse(null);
    }

    public List<Products> findAllByCategory_Id(Long categoryId) {
        return productsRepository.findAllByCategory_Id(categoryId);
    }

    public List<Products> findAllByBrand_Name(String brand_name) {
        return productsRepository.findAllByBrand_Name(brand_name);
    }
    public void deleted_byId(Long id) {
        productsRepository.deleteById(id);
    }


}
