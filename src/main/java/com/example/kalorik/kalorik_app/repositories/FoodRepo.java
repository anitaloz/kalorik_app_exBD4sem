package com.example.kalorik.kalorik_app.repositories;

import com.example.kalorik.kalorik_app.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface FoodRepo extends JpaRepository<Food, Long> {
    List<Food> findByName(String name);

    @Query(value = "SELECT f.name as name, f.calories as calories, f.bel as bel, f.fats as fats , f.ch as ch, mfi.quantity as quantity, mfi.unit as unit  FROM (meal_food_items mfi join meals m on mfi.meal_id=m.id) join food f on mfi.food_item_id=f.id WHERE meal_title=:mealTitle AND meal_date=:mealDate", nativeQuery = true)
    List<MealProductInfo> findByMealDateAndMealTitle(@Param("mealTitle") String mealTitle, @Param("mealDate") Date mealDate);

    List<Food> findByNameContainingIgnoreCase(String name);

    // Method using a native SQL query (if needed, for specific database functions)
    @Query(value = "SELECT SUM(calories) FROM food", nativeQuery = true)
    Double sumValueByNameNative(@Param("name") String name);
}