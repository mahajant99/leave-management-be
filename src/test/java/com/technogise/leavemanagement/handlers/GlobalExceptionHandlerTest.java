package com.technogise.leavemanagement.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.technogise.leavemanagement.dtos.ErrorResponse;
import com.technogise.leavemanagement.exceptions.LeaveNotFoundException;

@SpringBootTest
public class GlobalExceptionHandlerTest {

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    @DisplayName(value = "Given a leave does not , when Leave not found exception rises , it should return NOT_FOUNd status.")
    public void testLeaveNotFoundGlobalExceptionHandler() {
        Long nonExistentId = 1L;
        int statuscode = HttpStatus.NOT_FOUND.value();
        String errorMessage = "Leave not found with ID: " + nonExistentId;
        ErrorResponse errorResponse = new ErrorResponse(statuscode, errorMessage);

        @SuppressWarnings("rawtypes")
        ResponseEntity responseEntity = globalExceptionHandler
                .handleLeaveNotFoundException(new LeaveNotFoundException(nonExistentId));

        assertEquals(errorResponse.getStatusCode(), responseEntity.getStatusCode().value());
    }
}
