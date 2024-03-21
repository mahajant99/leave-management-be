package com.technogise.leavemanagement.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.DisplayName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.technogise.leavemanagement.dtos.LeaveDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.data.domain.Page;
import java.util.List;
import static org.hamcrest.Matchers.notNullValue;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.services.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @DisplayName("Given user ID, when retrieving leaves with pagination, then it should succeed")
    public void testRetrieveLeavesSuccess() throws Exception {
        Long userId = 1L;
        int page = 0;
        int size = 6;

        Leave leave = new Leave();
        leave.setId(2L);
        Page<Leave> mockPage = new PageImpl<>(List.of(leave), PageRequest.of(page, size), 1);

        when(leaveService.getLeavesByUserId(anyLong(), anyInt(), anyInt())).thenReturn(mockPage);

        mockMvc = MockMvcBuilders.standaloneSetup(leaveController).build();

        mockMvc.perform(get("/leaves/users/{userId}", userId)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", notNullValue()));
    }

    @Test
    @DisplayName("Given a user ID and pagination parameters, when retrieving leaves, then expect no content")
    public void testRetrieveLeavesNoContent() throws Exception {
        Long userId = 1L;
        int page = 0;
        int size = 6;
        Page<Leave> mockPage = Page.empty();

        when(leaveService.getLeavesByUserId(anyLong(), anyInt(), anyInt())).thenReturn(mockPage);

        mockMvc = MockMvcBuilders.standaloneSetup(leaveController).build();

        mockMvc.perform(get("/leaves/users/{userId}", userId)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Given a leave Id exists, when soft delete, then expect status OK")
    public void testSoftDeleteByLeaveIdForNoContent() throws Exception {
        Long id = 1L;

        doNothing().when(leaveService).deleteLeave(id);

        mockMvc = MockMvcBuilders.standaloneSetup(leaveController).build();

        mockMvc.perform(delete("/leaves/{leavesId}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void Should_ReturnCreatedResponse_When_LeaveDTOIsValid() throws Exception {
        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setUserId(1L);
        leaveDTO.setStartDate(LocalDate.of(2024, 03, 1));
        leaveDTO.setEndDate(LocalDate.of(2024, 03, 1));
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
}
