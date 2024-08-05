package com.tuskers.backend.commons.exceptions;

public class InvalidArgumentException extends IllegalArgumentException{
    public InvalidArgumentException(String message) {
        super(message);
    }
}
