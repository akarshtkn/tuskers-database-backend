package com.tuskers.backend.commons.exceptions;

public class InvalidParamsException extends IllegalArgumentException {
    public InvalidParamsException(String message) {
        super(message);
    }
}
