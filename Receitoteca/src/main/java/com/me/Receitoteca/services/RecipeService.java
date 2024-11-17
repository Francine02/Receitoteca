package com.me.Receitoteca.services;

import java.util.List;

import com.me.Receitoteca.dtos.RecipeDTO;

public interface RecipeService {
    List<RecipeDTO> findAllRecipes();
    RecipeDTO saveRecipe (RecipeDTO recipeDTO);
    RecipeDTO updateRecipe (RecipeDTO recipeDTO, Long id);
    void deleteRecipe (Long id);
    byte[] generateRecipePdf(String title) throws Exception;
} 
