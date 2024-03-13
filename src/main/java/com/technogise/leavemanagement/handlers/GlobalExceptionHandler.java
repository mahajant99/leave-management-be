package com.technogise.leavemanagement.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.technogise.leavemanagement.dtos.ErrorResponse;
import com.technogise.leavemanagement.exceptions.LeaveNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LeaveNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLeaveNotFoundException(LeaveNotFoundException leaveNotFoundException){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(),leaveNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }    
}
