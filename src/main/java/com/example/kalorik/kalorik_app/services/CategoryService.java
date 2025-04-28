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
        return categoryRepository.findByNameCaseInsensitive(name);
    }

    public List<Categories> getCategoriesByNameContaining(String name) {
        return categoryRepository.findByNameContaining(name);
    }

    public List<Categories> getCategoriesByIdsOrName(Set<Long> ids, String name) {
        return categoryRepository.findByIdsOrName(ids, name);
    }

    public List<Object[]> getCategoriesWithRecipeCount() {
        return categoryRepository.findCategoriesWithRecipeCount();
    }

    public Categories saveCategory(Categories category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public Categories updateCategory(Long id, Categories updatedCategory) {
        Optional<Categories> existingCategory = categoryRepository.findById(id);

        if (existingCategory.isPresent()) {
            Categories category = existingCategory.get();
            category.setName(updatedCategory.getName());
            // Update other fields as needed
            return categoryRepository.save(category);
        } else {
            // Handle the case where the category doesn't exist (e.g., throw an exception)
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
    }

    // Custom Exception for Resource Not Found
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
}
