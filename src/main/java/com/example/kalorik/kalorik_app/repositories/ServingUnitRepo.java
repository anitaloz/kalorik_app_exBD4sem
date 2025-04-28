package com.example.kalorik.kalorik_app.repositories;

import com.example.kalorik.kalorik_app.domain.MealFoodItems;
import com.example.kalorik.kalorik_app.domain.ServingUnits;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ServingUnitRepo extends CrudRepository<ServingUnits, Long> {
    ServingUnits findServingUnitsById(Long id);
}
