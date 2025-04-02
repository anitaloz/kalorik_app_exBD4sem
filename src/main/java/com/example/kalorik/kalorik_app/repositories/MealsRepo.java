package com.example.kalorik.kalorik_app.repositories;

import com.example.kalorik.kalorik_app.domain.Food;
import com.example.kalorik.kalorik_app.domain.Meals;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface MealsRepo extends CrudRepository<Meals, Long> {

}