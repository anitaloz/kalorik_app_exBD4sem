package com.example.kalorik.kalorik_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.kalorik.kalorik_app.domain.Recipes;

import java.util.List;
import java.util.List;
import java.util.Set;
@Repository
public interface RecipeRepository extends JpaRepository<Recipes, Long> {

    // Поиск рецептов по названию (регистронезависимый)
    List<Recipes> findByNameContainingIgnoreCase(String name);

    // Оптимизированный запрос для поиска по категории с явным JOIN
    @Query("SELECT distinct r FROM Recipes r JOIN FETCH r.categories c WHERE c.id = :categoryId")
    List<Recipes> findByCategoryId(@Param("categoryId") Long categoryId);

    //List<Recipes> findByCategories_Id(Long categoryId);


    // Использование IN для поиска по нескольким продуктам
    @Query("SELECT DISTINCT r FROM Recipes r JOIN FETCH r.products p WHERE p.id IN :productIds")
    List<Recipes> findByProductIds(@Param("productIds") Set<Long> productIds);

    // Оптимизированный запрос для поиска по нескольким категориям
    @Query("SELECT r FROM Recipes r JOIN r.categories c WHERE c.id IN :categoryIds " +
           "GROUP BY r.id HAVING COUNT(c.id) = :categoryCount")
    List<Recipes> findByAllCategories(@Param("categoryIds") Set<Long> categoryIds, 
                                   @Param("categoryCount") Long categoryCount);

    // Полнотекстовый поиск с использованием PostgreSQL tsvector
    @Query(value = """
           SELECT * FROM recipes 
           WHERE to_tsvector('english', name  ' '  text) @@ to_tsquery('english', :query)
           """, nativeQuery = true)
    List<Recipes> fullTextSearch(@Param("query") String query);
}