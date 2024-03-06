package com.technogise.leavemanagement.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ValidationHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidaitonErrors(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        List<ObjectError> retrievedErrors = exception.getBindingResult().getAllErrors();

        retrievedErrors.forEach(err -> {
            FieldError fe = (FieldError) err;
            errors.put(fe.getField(), fe.getDefaultMessage());
        });
        return errors;
    }
}
