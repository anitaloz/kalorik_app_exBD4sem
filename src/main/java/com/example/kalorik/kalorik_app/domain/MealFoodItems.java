package com.example.kalorik.kalorik_app.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;


//Таблица приемов пищи
@Entity
@Table(name="meal_food_items")
public class MealFoodItems {
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

    public MealFoodItems() {
    }

    public MealFoodItems(MealFoodItemId id, Meals meal, Food food, Float quantity) {
        this.id = id;
        this.meal = meal;
        this.food = food;
        this.quantity = quantity;
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
        MealFoodItems that = (MealFoodItems) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}