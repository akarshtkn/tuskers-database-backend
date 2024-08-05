package com.tuskers.backend.commons.exceptions;

import com.tuskers.backend.commons.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//        StringBuilder strBuilder = new StringBuilder();
//
//        e.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName;
//            try {
//                fieldName = ((FieldError) error).getField();
//
//            } catch (ClassCastException ex) {
//                fieldName = error.getObjectName();
//            }
//            String message = error.getDefaultMessage();
//            strBuilder.append(String.format("%s: %s\n", fieldName, message));
//        });
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(errorMapper.createErrorMap(strBuilder.substring(0, strBuilder.length()-1)));
//
//    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception){
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidParamsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidParamsException(InvalidParamsException exception) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidArgumentException(InvalidArgumentException exception) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
