package com.fatec.biblioteca.api.exception;

/**
 * Exception for resource not found errors.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
