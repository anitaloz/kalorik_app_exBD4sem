package com.example.kalorik.kalorik_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.kalorik.kalorik_app.domain.Categories;

import java.util.List;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Long> {

    // Использование LOWER для регистронезависимого поиска
    Categories findByNameIgnoreCase(String name);

    // Оптимизированный поиск по части имени с индексацией
    @Query("SELECT c FROM Categories c WHERE c.name ILIKE %:name%")
    List<Categories> findByNameContaining(@Param("name") String name);

    //List<Categories> findByNameContainingIgnoreCase(String name);

    // Использование UNION ALL для эффективного поиска по нескольким критериям
    @Query(value = """
           (SELECT * FROM categories WHERE id IN :ids) 
           UNION ALL 
           (SELECT * FROM categories WHERE name ILIKE %:name% LIMIT 10)
           """, nativeQuery = true)
    List<Categories> findByIdsOrName(@Param("ids") Set<Long> ids, @Param("name") String name);

    // Получение категорий с количеством рецептов
    // @Query("SELECT c, COUNT(r) FROM Category c LEFT JOIN c.recipes r GROUP BY c.id")
    // List<Object[]> findCategoriesWithRecipeCount();
}