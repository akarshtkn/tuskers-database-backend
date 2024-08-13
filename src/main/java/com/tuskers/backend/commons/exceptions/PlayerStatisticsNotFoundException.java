package com.tuskers.backend.commons.exceptions;

public class PlayerStatisticsNotFoundException extends RuntimeException {
    public PlayerStatisticsNotFoundException(String message) {
        super(message);
    }
}
