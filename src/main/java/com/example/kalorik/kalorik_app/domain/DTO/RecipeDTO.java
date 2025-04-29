package com.example.kalorik.kalorik_app.domain.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {
    private Long id;
    private String name;
    private String text;
    private Set<Long> productIds;
    private Set<Long> categoryIds;
}
