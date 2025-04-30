package com.example.kalorik.kalorik_app.services;

import com.example.kalorik.kalorik_app.domain.Categories;
import com.example.kalorik.kalorik_app.domain.Food;
import com.example.kalorik.kalorik_app.domain.Recipes;
import com.example.kalorik.kalorik_app.repositories.RecipeRepository;
import com.example.kalorik.kalorik_app.services.CategoryService.ResourceNotFoundException;

import jakarta.persistence.criteria.Join;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    // 1. Получение всех рецептов
    @Transactional(readOnly = true)
    public List<Recipes> getAllRecipes() {
        return recipeRepository.findAll();
    }

    // 2. Поиск по названию (без учета регистра)
    @Transactional(readOnly = true)
    public List<Recipes> findByNameContainingIgnoreCase(String name) {
        return recipeRepository.findByNameContainingIgnoreCase(name);
    }

    // 3. Фильтрация по категории
    @Transactional(readOnly = true)
    public List<Recipes> findByCategoryId(Long categoryId) {
        return recipeRepository.findByCategoryId(categoryId);
    }

    // 4. Полнотекстовый поиск (если требуется)
    @Transactional(readOnly = true)
    public List<Recipes> fullTextSearch(String query) {
        return recipeRepository.fullTextSearch(query);
    }

    // // 5. Получение рецепта по ID (для детальной страницы)
    // @Transactional(readOnly = true)
    // public Recipes getRecipeById(Long id) {
    //     return recipeRepository.findById(id)
    //             .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
    // }

    @Transactional(readOnly = true)
    public List<Recipes> findByProductIds(List<Long> productIds) {
        return recipeRepository.findByProductIds(productIds);
    }

    @Transactional(readOnly = true)
    public List<Recipes> findBySearchParams(String name, Long categoryId, List<Long> productIds) {
        Specification<Recipes> spec = Specification.where(null);
        
        if (name != null && !name.isEmpty()) {
            spec = spec.and((root, query, cb) -> 
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        
        if (categoryId != null) {
            spec = spec.and((root, query, cb) -> {
                Join<Recipes, Categories> categories = root.join("categories");
                return cb.equal(categories.get("id"), categoryId);
            });
        }
        
        if (productIds != null && !productIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                // Для поиска рецептов, содержащих ВСЕ выбранные продукты
                Join<Recipes, Food> products = root.join("products");
                return products.get("id").in(productIds);
            });
            
            // Добавляем distinct через отдельный Specification
            spec = spec.and(distinct());
        }
        
        return recipeRepository.findAll(spec);
    }
    
    // Вспомогательный метод для distinct
    private Specification<Recipes> distinct() {
        return (root, query, cb) -> {
            query.distinct(true);
            return null;
        };
    }

     @Transactional(readOnly = true)
    public Recipes getRecipeWithDetails(Long id) {
        return recipeRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Рецепт с ID " + id + " не найден"));
    }
}




// @Service
// public class RecipeService {

//     private final RecipeRepository recipeRepository;

//     @Autowired
//     public RecipeService(RecipeRepository recipeRepository) {
//         this.recipeRepository = recipeRepository;
//     }

//     public List<Recipes> getAllRecipes() {
//         return recipeRepository.findAll();
//     }

//     public Recipes getRecipeById(Long id) {
//         return recipeRepository.findById(id).orElse(null); //orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));
//     }

//     public List<Recipes> findByNameContainingIgnoreCase(String name) {
//         return recipeRepository.findByNameContainingIgnoreCase(name);
//     }

//     public List<Recipes> findByCategoryId(Long categoryId) {
//         return recipeRepository.findByCategoryId(categoryId);
//     }

//     // public List<Recipes> findByCategoryId(Long categoryId) {
//     //     return recipeRepository.findByCategories_Id(categoryId);
//     // }

//     public List<Recipes> findByProductIds(Set<Long> productIds) {
//         return recipeRepository.findByProductIds(productIds);
//     }

//     public List<Recipes> findByAllCategories(Set<Long> categoryIds) {
//         return recipeRepository.findByAllCategories(categoryIds, (long) categoryIds.size());
//     }

//     public List<Recipes> fullTextSearch(String query) {
//         return recipeRepository.fullTextSearch(query);
//     }

//     public Recipes saveRecipe(Recipes recipe) {
//         return recipeRepository.save(recipe);
//     }

// }
// @Service
// public class RecipeService {
//     private final RecipeRepository recipeRepository;

//     @Autowired
//     public RecipeService(RecipeRepository recipeRepository) {
//         this.recipeRepository = recipeRepository;
//     }

//     @Transactional(readOnly = true)
//     public List<Recipes> getAllRecipes() {
//         List<Recipes> recipes = recipeRepository.findAll();
//         // Инициализация необходимых коллекций
//         recipes.forEach(recipe -> {
//             if(recipe.getProducts() != null) {
//                 recipe.getProducts().size(); // Инициализация ленивой коллекции
//             }
//             if(recipe.getCategories() != null) {
//                 recipe.getCategories().size();
//             }
//         });
//         return recipes;
//     }
// }

