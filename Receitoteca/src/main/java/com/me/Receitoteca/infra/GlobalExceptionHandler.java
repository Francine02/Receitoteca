package com.me.Receitoteca.infra;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.me.Receitoteca.exceptions.DuplicateRecipeException;
import com.me.Receitoteca.exceptions.InvalidRecipeException;
import com.me.Receitoteca.exceptions.RecipeNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<String> handleRecipeNotFound(RecipeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(DuplicateRecipeException.class)
    public ResponseEntity<String> handleDuplicateRecipe(DuplicateRecipeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(InvalidRecipeException.class)
    public ResponseEntity<String> handleInvalidRecipe(InvalidRecipeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
