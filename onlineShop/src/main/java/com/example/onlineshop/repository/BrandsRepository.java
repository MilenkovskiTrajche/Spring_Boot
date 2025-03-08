package com.example.onlineshop.repository;

import com.example.onlineshop.entity.Brands;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.jpa.repository.JpaRepository;

@ReadingConverter
public interface BrandsRepository extends JpaRepository<Brands, Long> {
}
