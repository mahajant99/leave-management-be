package com.technogise.leavemanagement.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.technogise.leavemanagement.controllers.LeaveController;
import com.technogise.leavemanagement.dtos.LeaveDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ValidationHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LeaveController leaveController;

    @Test
    public void Should_ReturnValidationMessages_When_LeaveDTOIsInvalid() throws Exception {

        List<String> validationMessages = Arrays.asList("Leave Type is required", "Start Date is required", "End Date is required", "Description is required");
        Long userId = 1L;
        LeaveDTO leaveDTO = LeaveDTO.builder()
                      .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody = objectMapper.writeValueAsString(leaveDTO);

        mockMvc = MockMvcBuilders.standaloneSetup(leaveController)
                       .setControllerAdvice(new ValidationHandler()) 
                       .build();

        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(userId.toString());

        mockMvc.perform(post("/v1/oauth/leaves")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.leaveType").value(validationMessages.get(0)))
                .andExpect(jsonPath("$.startDate").value(validationMessages.get(1)))
                .andExpect(jsonPath("$.endDate").value(validationMessages.get(2)))
                .andExpect(jsonPath("$.description").value(validationMessages.get(3)));
    }
}
