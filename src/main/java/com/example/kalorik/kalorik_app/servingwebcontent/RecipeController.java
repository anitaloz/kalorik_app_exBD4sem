package com.example.kalorik.kalorik_app.servingwebcontent;

import com.example.kalorik.kalorik_app.domain.Recipes;
import com.example.kalorik.kalorik_app.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.kalorik.kalorik_app.domain.Recipes;
import com.example.kalorik.kalorik_app.services.CategoryService;
import com.example.kalorik.kalorik_app.services.CategoryService.ResourceNotFoundException;
import com.example.kalorik.kalorik_app.services.FoodService;
import com.example.kalorik.kalorik_app.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/recipes")
public class RecipeController {
    
    private final RecipeService recipeService;
    private final CategoryService categoryService;
    private final FoodService productService; // Новый сервис

    @Autowired
    public RecipeController(RecipeService recipeService, 
                          CategoryService categoryService,
                          FoodService productService) {
        this.recipeService = recipeService;
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping
    public String showRecipes(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) List<Long> productIds, // Новый параметр
        @RequestParam(required = false) Long recipeId,
        Model model) {
        
        List<Recipes> recipes;
        
        if (search != null || categoryId != null || (productIds != null && !productIds.isEmpty())) {
            recipes = recipeService.findBySearchParams(search, categoryId, productIds);
        }else if (search != null && !search.isEmpty()) {
            recipes = recipeService.findByNameContainingIgnoreCase(search);
            model.addAttribute("searchQuery", search);
        } else if (categoryId != null) {
            recipes = recipeService.findByCategoryId(categoryId);
            model.addAttribute("selectedCategoryId", categoryId);
        } else if (productIds != null && !productIds.isEmpty()) {
            recipes = recipeService.findByProductIds(productIds);
            model.addAttribute("selectedProductIds", productIds);
        } else {
            recipes = recipeService.getAllRecipes();
        }

        model.addAttribute("allProducts", productService.getAllProducts());
        if (productIds != null) {
            model.addAttribute("selectedProductIds", productIds);
        }

        //добавлено если что удалить
        if (recipeId != null) {
            Recipes fullRecipe = recipeService.getRecipeById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
            model.addAttribute("fullRecipe", fullRecipe);
        }
        
        model.addAttribute("recipes", recipes);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("allProducts", productService.getAllProducts());
        model.addAttribute("searchQuery", search);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedProductIds", productIds);
        
        return "recipes";
    }
}

// @Controller
// @RequestMapping("/recipes")
// public class RecipeController {
    
//     private final RecipeService recipeService;

//     public RecipeController(RecipeService recipeService) {
//         this.recipeService = recipeService;
//     }

//     @GetMapping
//     public String getAllRecipes(Model model) {
//         model.addAttribute("recipes", recipeService.getAllRecipes());
//         return "recipes/list";
//     }

//     @GetMapping("/{id}")
//     public String getRecipeById(@PathVariable Long id, Model model) {
//         Recipes recipe = recipeService.getRecipeById(id);
//         if (recipe == null) {
//             return "redirect:/error";
//         }
//         model.addAttribute("recipe", recipe);
//         return "recipes/view";
//     }

//     @GetMapping("/search")
//     public String searchRecipes(
//             @RequestParam(required = false) String name,
//             Model model) {
        
//         List<Recipes> recipes;
//         if (name != null && !name.isEmpty()) {
//             recipes = recipeService.findByNameContainingIgnoreCase(name);
//         } else {
//             recipes = recipeService.getAllRecipes();
//         }
        
//         model.addAttribute("recipes", recipes);
//         return "recipes/list";
//     }

//     @GetMapping("/new")
//     public String showCreateForm(Model model) {
//         model.addAttribute("recipe", new Recipes());
//         return "recipes/form";
//     }

//     @PostMapping
//     public String createRecipe(@ModelAttribute Recipes recipe) {
//         recipeService.saveRecipe(recipe);
//         return "redirect:/recipes";
//     }

//     @GetMapping("/{id}/edit")
//     public String showEditForm(@PathVariable Long id, Model model) {
//         Recipes recipe = recipeService.getRecipeById(id);
//         if (recipe == null) {
//             return "redirect:/error";
//         }
//         model.addAttribute("recipe", recipe);
//         return "recipes/form";
//     }

//     // @PostMapping("/{id}")
//     // public String updateRecipe(@PathVariable Long id, @ModelAttribute Recipes recipe) {
//     //     recipeService.updateRecipe(id, recipe);
//     //     return "redirect:/recipes";
//     // }

//     // @PostMapping("/{id}/delete")
//     // public String deleteRecipe(@PathVariable Long id) {
//     //     recipeService.deleteRecipe(id);
//     //     return "redirect:/recipes";
//     // }
// }

// RecipeController.java



// @Controller
// @RequestMapping("/recipes")
// public class RecipeController {

//     private final RecipeService recipeService;
//     private final CategoryService categoryService;

//     public RecipeController(RecipeService recipeService, CategoryService categoryService) {
//         this.recipeService = recipeService;
//         this.categoryService = categoryService;
//     }

//     @GetMapping
//     public String showRecipesPage(
//             @RequestParam(required = false) String search,
//             @RequestParam(required = false) String categoryId,
//             @RequestParam(required = false) String productIds,
//             @RequestParam(required = false) String fullTextSearch,
//             Model model) {

//         List<Recipes> recipes;
        
//         if (search != null && !search.isEmpty()) {
//             // Поиск по названию
//             recipes = recipeService.findByNameContainingIgnoreCase(search);
//         } else if (categoryId != null && !categoryId.isEmpty()) {
//             // Поиск по категории
//             recipes = recipeService.findByCategoryId(Long.parseLong(categoryId));
//         } else if (productIds != null && !productIds.isEmpty()) {
//             // Поиск по продуктам
//             Set<Long> ids = Arrays.stream(productIds.split(","))
//                     .map(Long::parseLong)
//                     .collect(Collectors.toSet());
//             recipes = recipeService.findByProductIds(ids);
//         } else if (fullTextSearch != null && !fullTextSearch.isEmpty()) {
//             // Полнотекстовый поиск
//             recipes = recipeService.fullTextSearch(fullTextSearch);
//         } else {
//             // Все рецепты
//             recipes = recipeService.getAllRecipes();
//         }

//         model.addAttribute("recipes", recipes);
//         model.addAttribute("categories", categoryService.getAllCategories());
//         return "recipes";
//     }
// }
// @Controller
// @RequestMapping("/recipes")
// public class RecipeController {
    
//     private final RecipeService recipeService;
//     private final CategoryService categoryService;

//     public RecipeController(RecipeService recipeService, CategoryService categoryService) {
//         this.recipeService = recipeService;
//         this.categoryService = categoryService;
//     }

//     @GetMapping
//     public String showRecipes(Model model) {
//         try {
//             List<Recipes> recipes = recipeService.getAllRecipes();
//             if(recipes == null || recipes.isEmpty()) {
//                 model.addAttribute("message", "Рецепты не найдены");
//                 return "recipes";
//             }
            
//             model.addAttribute("recipes", recipes);
//             model.addAttribute("categories", categoryService.getAllCategories());
//             return "recipes";
//         } catch (Exception e) {
//             model.addAttribute("error", "Ошибка при загрузке рецептов: " + e.getMessage());
//             return "error";
//         }
//     }
// }
