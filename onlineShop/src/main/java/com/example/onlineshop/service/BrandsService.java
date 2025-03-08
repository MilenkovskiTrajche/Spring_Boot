package com.example.onlineshop.service;

import com.example.onlineshop.entity.Brands;
import com.example.onlineshop.repository.BrandsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandsService {

    private final BrandsRepository brandsRepository;

    public BrandsService(BrandsRepository brandsRepository) {
        this.brandsRepository = brandsRepository;
    }

    public List<Brands> findAll() {
        return brandsRepository.findAll();
    }

    public void save(Brands brands) {
        brandsRepository.save(brands);
    }
}
