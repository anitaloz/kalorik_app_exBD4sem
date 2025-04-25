package com.example.kalorik.kalorik_app.services;

import com.example.kalorik.kalorik_app.domain.Food;
import com.example.kalorik.kalorik_app.domain.MealFoodItems;
import com.example.kalorik.kalorik_app.domain.Meals;
import com.example.kalorik.kalorik_app.dopclass.MealProductInfo;
import com.example.kalorik.kalorik_app.repositories.Meal_food_itemsRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MealFoodItemService {
    private final Meal_food_itemsRepo itemRepo;

    public MealFoodItemService(Meal_food_itemsRepo itemRepo)
    {
        this.itemRepo=itemRepo;
    }

    public List<MealFoodItems> findMeal_food_itemsByMeal(Meals meals){
        return itemRepo.findMeal_food_itemsByMeal(meals);
    }

    public void save(MealFoodItems item)
    {
        itemRepo.save(item);
    }

    @Transactional
    public void deleteMealFoodItemsByMealAndFood(Meals meal, Food food){
        itemRepo.deleteMealFoodItemsByMealAndFood(meal, food);
    }

}
