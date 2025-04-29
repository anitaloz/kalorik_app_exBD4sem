package com.example.kalorik.kalorik_app.domain.DTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryWithCountResponse {
    private Long id;
    private String name;
    private Long recipeCount;
}
