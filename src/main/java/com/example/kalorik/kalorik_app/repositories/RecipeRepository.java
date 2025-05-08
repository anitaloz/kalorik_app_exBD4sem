package com.example.kalorik.kalorik_app.repositories;

import com.example.kalorik.kalorik_app.domain.Categories;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.kalorik.kalorik_app.domain.Recipes;

import java.util.List;
import java.util.Optional;
import java.util.List;
import java.util.Set;
@Repository
public interface RecipeRepository extends JpaRepository<Recipes, Long> {

    // Поиск рецептов по названию (регистронезависимый)
    List<Recipes> findByNameContainingIgnoreCase(String name);

    Optional<Recipes>findById(Long id);



    // Оптимизированный запрос для поиска по категории с явным JOIN
    @Query("SELECT distinct r FROM Recipes r JOIN FETCH r.categories c WHERE c.id = :categoryId")
    List<Recipes> findByCategoryId(@Param("categoryId") Long categoryId);

    //List<Recipes> findByCategories_Id(Long categoryId);
//    @Query("SELECT r FROM Recipes r WHERE (:c1 IS NULL OR r.categories = :c1) " +
//            "AND (:c2 IS NULL OR r.categories = :c2) " +
//            "AND (:c3 IS NULL OR r.categories = :c3)")
//    List<Recipes> findRecipesByOptionalCategories(
//            @Param("c1") Categories c1,
//            @Param("c2") Categories c2,
//            @Param("c3") Categories c3
//    );

    // Использование IN для поиска по нескольким продуктам
    @Query("SELECT DISTINCT r FROM Recipes r JOIN FETCH r.products p WHERE p.id IN :productIds")
    List<Recipes> findByProductIds(@Param("productIds") List<Long> productIds);

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

    // @Query("SELECT DISTINCT r FROM Recipes r JOIN r.products p WHERE p.id IN :productIds")
    // List<Recipes> findByProducts_IdIn(@Param("productIds") List<Long> productIds);

     // Базовый метод для поиска с фильтрами
    @EntityGraph(attributePaths = {"categories", "products"})
    List<Recipes> findAll(Specification<Recipes> spec);

    @EntityGraph(attributePaths = {"products", "categories"})
    @Query("SELECT r FROM Recipes r WHERE r.id = :id")
    Optional<Recipes> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT r FROM Recipes r WHERE r.calories <= :maxCalories")
    List<Recipes> findByCaloriesLessThanEqual(@Param("maxCalories") double maxCalories);

}