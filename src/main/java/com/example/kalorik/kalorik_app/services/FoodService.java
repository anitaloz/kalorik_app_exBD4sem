package com.example.kalorik.kalorik_app.services;

import com.example.kalorik.kalorik_app.domain.Food;
import com.example.kalorik.kalorik_app.repositories.FoodRepo;
import com.example.kalorik.kalorik_app.dopclass.MealProductInfo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FoodService {

    private final FoodRepo foodRepo;

    public FoodService(FoodRepo foodRepo)
    {
        this.foodRepo=foodRepo;
    }

    public List<MealProductInfo> findByMealDateAndMealTitle(String mealTitle, Date mealDate, Long id)
    {
        return foodRepo.findByMealDateAndMealTitle(mealTitle, mealDate, id);
    }

    public List<Food> findByNameContainingIgnoreCase(String name){
        return foodRepo.findByNameContainingIgnoreCase(name);
    }

    public List<Food> findByName(String name){
        return foodRepo.findByName(name);
    }

    public Double sumValueByNameNative(String name) {
        return foodRepo.sumValueByNameNative(name);
    }

    public void save(Food food)
    {
        foodRepo.save(food);
    }
    public Food findFoodById(Long id)
    {
        return foodRepo.findFoodById(id);
    }
}
