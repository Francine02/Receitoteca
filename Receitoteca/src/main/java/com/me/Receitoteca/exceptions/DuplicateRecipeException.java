package com.me.Receitoteca.exceptions;

public class DuplicateRecipeException extends RuntimeException {
    public DuplicateRecipeException(String message) {
        super(message);
    }

    public DuplicateRecipeException(String message, Throwable cause) {
        super(message, cause);
    }
}
