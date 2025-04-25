package com.example.kalorik.kalorik_app.repositories;

import com.example.kalorik.kalorik_app.domain.Food;
import com.example.kalorik.kalorik_app.domain.MealFoodItems;
import com.example.kalorik.kalorik_app.domain.Meals;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Meal_food_itemsRepo extends CrudRepository<MealFoodItems, Long> {
    List<MealFoodItems> findMeal_food_itemsByMeal(Meals meals);

    void deleteMealFoodItemsByMealAndFood(Meals meal, Food food);
}