package com.technogise.leavemanagement.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.technogise.leavemanagement.exceptions.LeaveAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import com.technogise.leavemanagement.dtos.ErrorResponse;
import com.technogise.leavemanagement.exceptions.LeaveNotFoundException;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    @DisplayName(value = "Given a leave does not exists , when Leave not found exception rises , then should return NOT_FOUND status.")
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

    @Test
    public void Should_HandleLeaveAlreadyExistsException_When_LeaveAlreadyExistsExceptionIsThrown() throws Exception {
        int statusCode = HttpStatus.BAD_REQUEST.value();
        String errorMessage = "Leave already exists";
        ErrorResponse errorResponse = new ErrorResponse(statusCode, errorMessage);

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler
                .handleLeaveAlreadyExistsException(new LeaveAlreadyExistsException());

        assertEquals(statusCode, responseEntity.getStatusCode().value());
        assertEquals(errorResponse, responseEntity.getBody());
    }

    @Test
    @DisplayName("Given an IllegalArgumentException with 'Email domain not allowed' message, when handled, then should return BAD_REQUEST status.")
    public void testHandleIllegalArgumentException() {
        String exceptionMessage = "Email domain not allowed";
        int statusCode = HttpStatus.BAD_REQUEST.value();
        ErrorResponse expectedErrorResponse = new ErrorResponse(statusCode, exceptionMessage);

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler
                .handleIllegalArgumentException(new IllegalArgumentException(exceptionMessage));

        assertEquals(expectedErrorResponse.getStatusCode(), responseEntity.getStatusCode().value());
    }

    @Test
    @DisplayName("Given an IllegalArgumentException with a generic message, when handled, then should return INTERNAL_SERVER_ERROR status.")
    public void testHandleGenericIllegalArgumentException() {
        String exceptionMessage = "A generic error occurred";
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        ErrorResponse expectedErrorResponse = new ErrorResponse(statusCode, "An error occurred");
        
        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler
                .handleIllegalArgumentException(new IllegalArgumentException(exceptionMessage));

        assertEquals(expectedErrorResponse.getStatusCode(), responseEntity.getStatusCode().value());
    }
}
