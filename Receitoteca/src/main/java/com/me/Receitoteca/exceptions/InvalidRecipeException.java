package com.me.Receitoteca.exceptions;

public class InvalidRecipeException extends RuntimeException {
    public InvalidRecipeException(String message) {
        super(message);
    }

    public InvalidRecipeException(String message, Throwable cause) {
        super(message, cause);
    }
}
