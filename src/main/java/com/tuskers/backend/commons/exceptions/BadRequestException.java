package com.tuskers.backend.commons.exceptions;

public class BadRequestException extends IllegalArgumentException{
    public BadRequestException(String message){
        super(message);
    }
}
