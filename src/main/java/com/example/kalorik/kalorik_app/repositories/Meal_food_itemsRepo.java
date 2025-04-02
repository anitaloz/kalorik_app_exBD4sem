package com.example.kalorik.kalorik_app.repositories;

import com.example.kalorik.kalorik_app.domain.Food;
import com.example.kalorik.kalorik_app.domain.Meal_food_items;
import com.example.kalorik.kalorik_app.domain.Meals;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Meal_food_itemsRepo extends CrudRepository<Meal_food_items, Long> {

}