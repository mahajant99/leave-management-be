package com.technogise.leavemanagement.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.technogise.leavemanagement.enums.LeaveType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;

import com.technogise.leavemanagement.dtos.ErrorResponse;
import com.technogise.leavemanagement.exceptions.LeaveNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.technogise.leavemanagement.dtos.LeaveDTO;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private MockMvc mockMvc;

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
    public void Should_ReturnUserNotFound_When_UserIdIsInvalid() throws Exception {
        LeaveDTO leaveDTO = LeaveDTO.builder()
                .startDate(LocalDate.of(2024, 3, 16))
                .endDate(LocalDate.of(2024, 3, 17))
                .description("Vacation")
                .userId(12L)
                .leaveType(String.valueOf(LeaveType.FULLDAY))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody = objectMapper.writeValueAsString(leaveDTO);

        mockMvc.perform(post("/leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("User not found: " + leaveDTO.getUserId()));

    }
}
