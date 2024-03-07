package com.technogise.leavemanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.technogise.leavemanagement.dtos.LeaveDTO;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.repositories.LeaveRepository;
import com.technogise.leavemanagement.services.LeaveService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class LeaveControllerTest {
    @Mock
    private LeaveService leaveService;

    @InjectMocks
    private LeaveController leaveController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void Should_ReturnCreatedResponse_When_LeaveDTOIsValid() throws Exception {
        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setUserId(1L);
        leaveDTO.setStartDate(LocalDate.of(2024, 03, 1)); // Corrected month format
        leaveDTO.setEndDate(LocalDate.of(2024, 03, 1)); // Corrected month format
        leaveDTO.setLeaveType("full day");
        leaveDTO.setDescription("Vacation leave");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody = objectMapper.writeValueAsString(leaveDTO);

        mockMvc.perform(post("/leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    public void Should_ReturnStartDateRequired_When_StartDateIsNotGiven() throws Exception {
        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setUserId(1L);
        leaveDTO.setEndDate(LocalDate.of(2024, 03, 1));
        leaveDTO.setLeaveType("full day");
        leaveDTO.setDescription("Vacation leave");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody = objectMapper.writeValueAsString(leaveDTO);

        mockMvc.perform(post("/leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(jsonPath("$.startDate").value("Start Date is required"));
    }

    @Test
    public void Should_ReturnEndDateRequired_When_EndDateIsNotGiven() throws Exception {
        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setUserId(1L);
        leaveDTO.setStartDate(LocalDate.of(2024, 03, 1));
        leaveDTO.setLeaveType("full day");
        leaveDTO.setDescription("Vacation leave");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody = objectMapper.writeValueAsString(leaveDTO);

        mockMvc.perform(post("/leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(jsonPath("$.endDate").value("End Date is required"));
    }

    @Test
    public void Should_ReturnLeaveTypeRequired_When_LeaveTypeIsNotGiven() throws Exception {
        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setUserId(1L);
        leaveDTO.setStartDate(LocalDate.of(2024, 03, 1));
        leaveDTO.setEndDate((LocalDate.of(2024,03,1)));
        leaveDTO.setDescription("Vacation leave");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody = objectMapper.writeValueAsString(leaveDTO);

        mockMvc.perform(post("/leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(jsonPath("$.leaveType").value("Leave Type is required"));
    }

    @Test
    public void Should_ReturnDescriptionRequired_When_DescriptionIsNotGiven() throws Exception {
        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setUserId(1L);
        leaveDTO.setStartDate(LocalDate.of(2024, 03, 1));
        leaveDTO.setEndDate((LocalDate.of(2024,03,1)));
        leaveDTO.setLeaveType("full day");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody = objectMapper.writeValueAsString(leaveDTO);

        mockMvc.perform(post("/leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(jsonPath("$.description").value("Description is required"));
    }

    @Test
    public void Should_ReturnInternalServerError_When_ResponseHasEmptyList() throws Exception {
        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setUserId(1L);
        leaveDTO.setStartDate(LocalDate.of(2024, 03, 1));
        leaveDTO.setEndDate(LocalDate.of(2024, 03, 1));
        leaveDTO.setLeaveType("full day");
        leaveDTO.setDescription("Vacation leave");

        when(leaveService.addLeaves(any(LeaveDTO.class))).thenReturn(Collections.emptyList());

        ResponseEntity<List<Leave>> response = leaveController.addLeaves(leaveDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
