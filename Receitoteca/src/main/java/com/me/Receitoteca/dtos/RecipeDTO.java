package com.me.Receitoteca.dtos;

import java.util.List;

import com.me.Receitoteca.enums.Category;
import com.me.Receitoteca.enums.Difficulty;

public record RecipeDTO(
    Long id,
    String title,
    List<String> ingredients,
    String preparation,
    String imgUrl,
    int prepTime,
    int cookTime,
    Category category,
    Difficulty difficulty
) {}
