package com.example.kalorik.kalorik_app.services;

import com.example.kalorik.kalorik_app.domain.Meals;
import com.example.kalorik.kalorik_app.domain.User;
import com.example.kalorik.kalorik_app.dopclass.MealProductInfo;
import com.example.kalorik.kalorik_app.repositories.MealsRepo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MealsService {

    private final MealsRepo mealsRepo;

    public MealsService(MealsRepo mealsRepo)
    {
        this.mealsRepo=mealsRepo;
    }

    public List<Meals> findMealsByMealDateAndMealTitleAndUser(Date date, String meal_title, User user){
        return mealsRepo.findMealsByMealDateAndMealTitleAndUser(date, meal_title, user);
    }

    public List<Meals> findMealsByMealDateAndUser(Date date, User user){
        return mealsRepo.findMealsByMealDateAndUser(date, user);
    }

    public void save(Meals meal)
    {
        mealsRepo.save(meal);
    }

    public void delete(Meals meal)
    {
        mealsRepo.delete(meal);
    }
}
