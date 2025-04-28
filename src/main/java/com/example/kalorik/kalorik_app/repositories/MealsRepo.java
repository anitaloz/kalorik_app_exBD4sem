package com.example.kalorik.kalorik_app.repositories;

import com.example.kalorik.kalorik_app.domain.Food;
import com.example.kalorik.kalorik_app.domain.Meals;
import com.example.kalorik.kalorik_app.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface MealsRepo extends CrudRepository<Meals, Long> {
    List<Meals> findMealsByMealDateAndMealTitleAndUser(Date date, String meal_title, User user);

}