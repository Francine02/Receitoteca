package com.me.Receitoteca.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.me.Receitoteca.entities.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByTitle(String title);
}
