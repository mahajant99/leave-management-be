package com.technogise.leavemanagement.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.technogise.leavemanagement.dtos.ErrorResponse;
import com.technogise.leavemanagement.exceptions.LeaveNotFoundException;

@WebMvcTest(GlobalExceptionHandler.class)
public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    public void testLeaveNotFoundGlobalExceptionHandler(){
        Long nonExistentId = 1L;
        int statuscode = HttpStatus.NOT_FOUND.value();
        String errorMessage = "Leave not found with ID: " + nonExistentId;
        ErrorResponse errorResponse = new ErrorResponse(statuscode, errorMessage);

        @SuppressWarnings("rawtypes")
        ResponseEntity responseEntity = globalExceptionHandler.handleLeaveNotFoundException(new LeaveNotFoundException(nonExistentId));

        assertEquals(errorResponse, responseEntity.getBody());
        assertEquals(statuscode, responseEntity.getStatusCode());
    }
}
