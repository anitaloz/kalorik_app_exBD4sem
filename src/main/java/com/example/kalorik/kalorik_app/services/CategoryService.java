package com.example.kalorik.kalorik_app.services;

import com.example.kalorik.kalorik_app.domain.Categories;
import com.example.kalorik.kalorik_app.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Categories> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Categories> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Categories getCategoryByNameCaseInsensitive(String name) {
        return categoryRepository.findByNameIgnoreCase(name);
    }

    public Categories getCategoryByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name);
    }

    public List<Categories> getCategoriesByNameContaining(String name) {
        return categoryRepository.findByNameContaining(name);
    }

    public List<Categories> getCategoriesByIdsOrName(Set<Long> ids, String name) {
        return categoryRepository.findByIdsOrName(ids, name);
    }


    public Categories saveCategory(Categories category) {
        return categoryRepository.save(category);
    }


    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
}
