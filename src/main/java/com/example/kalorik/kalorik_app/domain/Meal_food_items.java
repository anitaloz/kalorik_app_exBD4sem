package com.example.kalorik.kalorik_app.domain;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


//Таблица приемов пищи
@Entity
public class Meal_food_items {
    @EmbeddedId
    private MealFoodItemId id;
    @ManyToOne
    @MapsId("mealId")
    @JoinColumn(name = "meal_id")
    private Meals meal;

    @ManyToOne
    @MapsId("foodItemId")
    @JoinColumn(name = "food_item_id")
    private Food food;

    @Column(nullable = false)
    private Float quantity;

    @Column(length = 50)
    private String unit;

    public Meal_food_items() {
    }

    public Meal_food_items(MealFoodItemId id, Meals meal, Food food, Float quantity, String unit) {
        this.id = id;
        this.meal = meal;
        this.food = food;
        this.quantity = quantity;
        this.unit = unit;
    }

    public MealFoodItemId getId() {
        return id;
    }

    public void setId(MealFoodItemId id) {
        this.id = id;
    }

    public Meals getMeal() {
        return meal;
    }

    public void setMeal(Meals meal) {
        this.meal = meal;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Embeddable
    public static class MealFoodItemId implements Serializable {
        private Long mealId;
        private Long foodItemId;

        public MealFoodItemId() {}

        public MealFoodItemId(Long mealId, Long foodItemId) {
            this.mealId = mealId;
            this.foodItemId = foodItemId;
        }

        public Long getMealId() {
            return mealId;
        }

        public void setMealId(Long mealId) {
            this.mealId = mealId;
        }

        public Long getFoodItemId() {
            return foodItemId;
        }

        public void setFoodItemId(Long foodItemId) {
            this.foodItemId = foodItemId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MealFoodItemId that = (MealFoodItemId) o;
            return Objects.equals(mealId, that.mealId) && Objects.equals(foodItemId, that.foodItemId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mealId, foodItemId);
        }
    }

    //Equals и Hashcode, реализованные для ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal_food_items that = (Meal_food_items) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}