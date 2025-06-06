package com.example.kalorik.kalorik_app.servingwebcontent;

import com.example.kalorik.kalorik_app.domain.MealFoodItems;
import com.example.kalorik.kalorik_app.domain.Meals;
import com.example.kalorik.kalorik_app.domain.Recipes;
import com.example.kalorik.kalorik_app.domain.User;
import com.example.kalorik.kalorik_app.domain.UserInfo;
import com.example.kalorik.kalorik_app.repositories.Meal_food_itemsRepo;
import com.example.kalorik.kalorik_app.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.kalorik.kalorik_app.domain.Recipes;
import com.example.kalorik.kalorik_app.services.CategoryService.ResourceNotFoundException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Set;

// @Controller
// @RequestMapping("/recipes")
// public class RecipeController {
    
//     private final RecipeService recipeService;
//     private final CategoryService categoryService;
//     private final FoodService productService; // Новый сервис
//     @Autowired
//     private UserInfoService userInfoService;
//     @Autowired
//     private UserService userService;
//     @Autowired
//     public RecipeController(RecipeService recipeService, 
//                           CategoryService categoryService,
//                           FoodService productService) {
//         this.recipeService = recipeService;
//         this.categoryService = categoryService;
//         this.productService = productService;
//     }

//     @GetMapping
//     public String showRecipes(
//         @RequestParam(required = false) String search,
//         @RequestParam(required = false) Long categoryId,
//         @RequestParam(required = false) List<Long> productIds, // Новый параметр
//         @RequestParam(required = false) Long recipeId,
//         Model model) {
//         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//         User u=userService.findByUsername(auth.getName());
//         List<Recipes> recipes;
        
//         if (search != null || categoryId != null || (productIds != null && !productIds.isEmpty())) {
//             recipes = recipeService.findBySearchParams(search, categoryId, productIds);
//         }else if (search != null && !search.isEmpty()) {
//             recipes = recipeService.findByNameContainingIgnoreCase(search);
//             model.addAttribute("searchQuery", search);
//         } else if (categoryId != null) {
//             recipes = recipeService.findByCategoryId(categoryId);
//             model.addAttribute("selectedCategoryId", categoryId);
//         } else if (productIds != null && !productIds.isEmpty()) {
//             recipes = recipeService.findByProductIds(productIds);
//             model.addAttribute("selectedProductIds", productIds);
//         } else {
//             recipes = recipeService.getAllRecipes();
//         }

//         model.addAttribute("allProducts", productService.getAllProducts());
//         if (productIds != null) {
//             model.addAttribute("selectedProductIds", productIds);
//         }

//         //добавлено если что удалить
//         if (recipeId != null) {
//             Recipes fullRecipe = recipeService.getRecipeById(recipeId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
//             model.addAttribute("fullRecipe", fullRecipe);
//         }

//         model.addAttribute("recipes", recipes);
//         model.addAttribute("categories", categoryService.getAllCategories());
//         model.addAttribute("allProducts", productService.getAllProducts());
//         model.addAttribute("searchQuery", search);
//         model.addAttribute("selectedCategoryId", categoryId);
//         model.addAttribute("selectedProductIds", productIds);
//         String avatar=userInfoService.getUserInfoByUsr(u).getImageUrl();
//         model.addAttribute("avatar", avatar);
//         return "recipes";
//     }

    
// }


@Controller
@RequestMapping("/recipes")
public class RecipeController {
    
    private final RecipeService recipeService;
    private final CategoryService categoryService;
    private final FoodService productService;
    private final UserInfoService userInfoService;
    private final UserService userService;
    private final MealsService mealsService;
    private final Meal_food_itemsRepo mealFoodItemsRepository; // Добавляем репозиторий для доступа к продуктам

    @Autowired
    public RecipeController(RecipeService recipeService,
                          CategoryService categoryService,
                          FoodService productService,
                          UserInfoService userInfoService,
                          UserService userService,
                          MealsService mealsService,
                          Meal_food_itemsRepo mealFoodItemsRepository) {
        this.recipeService = recipeService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.userInfoService = userInfoService;
        this.userService = userService;
        this.mealsService = mealsService;
        this.mealFoodItemsRepository = mealFoodItemsRepository;
    }

    @GetMapping
    public String showRecipes(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) List<Long> productIds,
        @RequestParam(required = false) Long recipeId,
        @RequestParam(required = false) Boolean filterByCalories,
        Model model) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findByUsername(auth.getName());
        if (userInfoService.getUserInfoByUsr(currentUser)==null){
            return "redirect:/addInfo";
        }
        List<Recipes> recipes;
        System.out.println("filterByCalories: " + filterByCalories); // Логируем параметр
        // Рассчитываем оставшийся калораж, если требуется фильтрация
        Double remainingCalories = null;
        if (Boolean.TRUE.equals(filterByCalories)) {
            remainingCalories = calculateRemainingCalories(currentUser);
            System.out.println("Remaining calories: " + remainingCalories);
            model.addAttribute("remainingCalories", remainingCalories);
        }

        // Основная логика фильтрации
        recipes = applyFilters(search, categoryId, productIds, remainingCalories);

        // Добавляем атрибуты в модель
        addAttributesToModel(model, currentUser, recipes, 
                           search, categoryId, productIds, recipeId, 
                           filterByCalories, remainingCalories);
        
        return "recipes";
    }

    private Double calculateRemainingCalories(User user) {
        UserInfo userInfo = userInfoService.getUserInfoByUsr(user);
        if (userInfo == null || userInfo.getCaloriesnum() == null) {
            return null;
        }

        int dailyCalories = userInfo.getCaloriesnum();
        Date today = java.sql.Date.valueOf(LocalDate.now());
        System.out.println("Daily calories: " + dailyCalories);
        // Получаем все приемы пищи за сегодня (адаптируем под имеющийся метод)
        // List<Meals> todayMeals = new ArrayList<>();
        // for (String mealTitle : Arrays.asList("Завтрак", "Обед", "Ужин", "Перекус")) {
        //     todayMeals.addAll(mealsService.findMealsByMealDateAndMealTitleAndUser(today, mealTitle, user));
        // }
        List<Meals> todayMeals = mealsService.findMealsByMealDateAndUser(today, user);
        System.out.println("Found meals today: " + todayMeals.size());
        double consumedCalories = 0;
        for (Meals meal : todayMeals) {
            List<MealFoodItems> foodItems = mealFoodItemsRepository.findByMealId(meal.getId());
            for (MealFoodItems item : foodItems) {
                if (item.getFood() != null && item.getFood().getCalories() != null && item.getQuantity() != null) {
                    double itemCalories = (item.getFood().getCalories()/100) * item.getQuantity();
                    consumedCalories += itemCalories;
                    System.out.println("Added calories: " + itemCalories + 
                                      " from " + item.getFood().getName());
                }                
            }
        }
        System.out.println("Total consumed: " + consumedCalories);
        return Math.max(0, dailyCalories - consumedCalories);
    }

    private List<Recipes> applyFilters(String search, Long categoryId, 
                                     List<Long> productIds, Double maxCalories) {
        if (search != null || categoryId != null || (productIds != null && !productIds.isEmpty())) {
            return recipeService.findBySearchParamsWithCalories(
                search, categoryId, productIds, maxCalories);
        } else if (maxCalories != null) {
            return recipeService.findByCaloriesLessThanEqual(maxCalories);
        } else if (search != null && !search.isEmpty()) {
            return recipeService.findByNameContainingIgnoreCase(search);
        } else if (categoryId != null) {
            return recipeService.findByCategoryId(categoryId);
        } else if (productIds != null && !productIds.isEmpty()) {
            return recipeService.findByProductIds(productIds);
        } else {
            return recipeService.getAllRecipes();
        }
    }

    private void addAttributesToModel(Model model, User user, List<Recipes> recipes,
                                    String search, Long categoryId, List<Long> productIds,
                                    Long recipeId, Boolean filterByCalories, Double remainingCalories) {
        
        // Основные атрибуты
        model.addAttribute("recipes", recipes);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("allProducts", productService.getAllProducts());
        
        // Параметры фильтрации
        model.addAttribute("searchQuery", search);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedProductIds", productIds);
        model.addAttribute("filterByCalories", filterByCalories);
        
        // Информация о рецепте для модального окна
        if (recipeId != null) {
            Recipes fullRecipe = recipeService.getRecipeById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
            model.addAttribute("fullRecipe", fullRecipe);
        }
        
        // Информация пользователя
        UserInfo userInfo = userInfoService.getUserInfoByUsr(user);
        if (userInfo != null) {
            model.addAttribute("avatar", userInfo.getImageUrl());
        }
    }
}

