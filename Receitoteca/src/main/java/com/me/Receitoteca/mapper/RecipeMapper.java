package com.me.Receitoteca.mapper;

import com.me.Receitoteca.dtos.RecipeDTO;
import com.me.Receitoteca.entities.Recipe;

public class RecipeMapper {
    public static RecipeDTO toDTO(Recipe recipe) {
        return new RecipeDTO(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getIngredients(),
                recipe.getPreparation(),
                recipe.getImgUrl(),
                recipe.getPrepTime(),
                recipe.getCookTime(),
                recipe.getCategory(),
                recipe.getDifficulty());
    }

    public static Recipe toEntity(RecipeDTO recipeDTO) {
        Recipe recipe = new Recipe();
        recipe.setId(recipeDTO.id());
        recipe.setCategory(recipeDTO.category());
        recipe.setTitle(recipeDTO.title());
        recipe.setIngredients(recipeDTO.ingredients());
        recipe.setPreparation(recipeDTO.preparation());
        recipe.setImgUrl(recipeDTO.imgUrl());
        recipe.setPrepTime(recipeDTO.prepTime());
        recipe.setCookTime(recipeDTO.cookTime());
        recipe.setDifficulty(recipeDTO.difficulty());

        return recipe;
    }
}
