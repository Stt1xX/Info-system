package com.example.backend.exceptions;

public class SameUserExistsException extends Exception {
    public SameUserExistsException(String message) {
        super(message);
    }
}
