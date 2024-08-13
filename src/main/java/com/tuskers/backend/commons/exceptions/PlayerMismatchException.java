package com.tuskers.backend.commons.exceptions;

public class PlayerMismatchException extends RuntimeException {
    public PlayerMismatchException(String message) {
        super(message);
    }
}
