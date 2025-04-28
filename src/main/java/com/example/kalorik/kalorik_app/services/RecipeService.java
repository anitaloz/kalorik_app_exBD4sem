package com.example.kalorik.kalorik_app.services;

import com.example.kalorik.kalorik_app.domain.Recipes;
import com.example.kalorik.kalorik_app.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipes> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipes getRecipeById(Long id) {
        return recipeRepository.findById(id).orElse(null); //orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));
    }

    public List<Recipes> findByNameContainingIgnoreCase(String name) {
        return recipeRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Recipes> findByCategoryId(Long categoryId) {
        return recipeRepository.findByCategoryId(categoryId);
    }

    public List<Recipes> findByProductIds(Set<Long> productIds) {
        return recipeRepository.findByProductIds(productIds);
    }

    public List<Recipes> findByAllCategories(Set<Long> categoryIds) {
        return recipeRepository.findByAllCategories(categoryIds, (long) categoryIds.size());
    }

    public List<Recipes> fullTextSearch(String query) {
        return recipeRepository.fullTextSearch(query);
    }

    public Recipes saveRecipe(Recipes recipe) {
        return recipeRepository.save(recipe);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    public Recipes updateRecipe(Long id, Recipes recipeDetails) {
        Recipes recipe = recipeRepository.findById(id).orElse(null); //orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));

        if(recipe != null) {
            recipe.setName(recipeDetails.getName());
            recipe.setText(recipeDetails.getText());
            recipe.setCategories(recipeDetails.getCategories());
            recipe.setProducts(recipeDetails.getProducts());
            // Set other fields if necessary

            return recipeRepository.save(recipe);
        }
        return null;
    }
}
