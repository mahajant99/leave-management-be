package com.technogise.leavemanagement.controllers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.enums.LeaveType;
import com.technogise.leavemanagement.handlers.ValidationHandler;

import org.junit.jupiter.api.DisplayName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.technogise.leavemanagement.dtos.LeaveDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.data.domain.Page;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.services.LeaveService;

import io.jsonwebtoken.lang.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.security.Principal;
import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class LeaveControllerTest {
    @MockBean
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

    Principal principal = Mockito.mock(Principal.class);
    when(principal.getName()).thenReturn(userId.toString());

    mockMvc = MockMvcBuilders.standaloneSetup(leaveController).build();

    mockMvc.perform(get("/v1/oauth/leaves/users")
            .principal(principal)
            .param("page", String.valueOf(page))
            .param("size", String.valueOf(size)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(1)))
            .andExpect(jsonPath("$.content[0].id", notNullValue()));
    }

    @Test
    @DisplayName("Given leaves exist, when retrieving leaves with pagination, then it should succeed")
    public void testRetrieveAllLeavesSuccess() throws Exception {
        int page = 0;
        int size = 6;

        Leave leave = new Leave();
        leave.setId(2L);
        Page<Leave> mockPage = new PageImpl<>(List.of(leave), PageRequest.of(page, size), 1);

        when(leaveService.getAllLeaves(anyInt(), anyInt())).thenReturn(mockPage);

        mockMvc = MockMvcBuilders.standaloneSetup(leaveController).build();

        mockMvc.perform(get("/v1/oauth/leaves")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("Given a user ID and pagination parameters, when retrieving leaves, then expect no content")
    public void testRetrieveLeavesNoContent() throws Exception {
        Long userId = 1L;
        int page = 0;
        int size = 6;
        Page<Leave> mockPage = Page.empty();

        when(leaveService.getLeavesByUserId(anyLong(), anyInt(), anyInt())).thenReturn(mockPage);

        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(userId.toString());

        mockMvc = MockMvcBuilders.standaloneSetup(leaveController).build();

        mockMvc.perform(get("/v1/oauth/leaves/users")
                .principal(principal)
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

        mockMvc.perform(delete("/v1/oauth/leaves/{leavesId}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void Should_ReturnCreatedResponse_When_LeaveDTOIsValid() throws Exception {
        LeaveDTO leaveDTO = LeaveDTO.builder()
            .startDate(LocalDate.of(2024, 03, 1))
            .endDate(LocalDate.of(2024, 03, 1))
            .description("Vacation")
            .userId(1L)
            .leaveType(String.valueOf(LeaveType.FULLDAY))
            .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody = objectMapper.writeValueAsString(leaveDTO);

        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(leaveDTO.getUserId().toString());

        mockMvc = MockMvcBuilders.standaloneSetup(leaveController).build();

        mockMvc.perform(post("/v1/oauth/leaves")
                    .principal(principal)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().isCreated());
    }

    @Test
    public void Should_ReturnListOfLeaves_When_LeaveDTOHasDateInRange() throws Exception {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = LeaveDTO.builder()
                .startDate(LocalDate.of(2024, 04, 01))
                .endDate(LocalDate.of(2024, 04, 02))
                .description("Vacation")
                .userId(1l)
                .leaveType(String.valueOf(LeaveType.FULLDAY))
                .build();

        Leave leave1 = new Leave();
        leave1.setUser(user);
        leave1.setDate(LocalDate.of(2024, 04, 01));
        leave1.setDuration(1.0);
        leave1.setDescription("Vacation");
        leave1.setHalfDay(null);

        Leave leave2 = new Leave();
        leave2.setUser(user);
        leave2.setDate(LocalDate.of(2024, 04, 02));
        leave2.setDuration(1.0);
        leave2.setDescription("Vacation");
        leave2.setHalfDay(null);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody = objectMapper.writeValueAsString(leaveDTO);

        List<Leave> expectedLeaves = new ArrayList<>();
        expectedLeaves.add(leave1);
        expectedLeaves.add(leave2);
        
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(leaveDTO.getUserId().toString());
        when(leaveService.addLeaves(leaveDTO)).thenReturn(expectedLeaves);

        mockMvc = MockMvcBuilders.standaloneSetup(leaveController).build();

        mockMvc.perform(post("/v1/oauth/leaves")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].date").value(LocalDate.of(2024, 04, 01).toString()))
                .andExpect(jsonPath("$[0].duration").value(1.0))
                .andExpect(jsonPath("$[0].description").value("Vacation"))
                .andExpect(jsonPath("$[0].halfDay", nullValue()))
                .andExpect(jsonPath("$[1].date").value(LocalDate.of(2024, 04, 02).toString()))
                .andExpect(jsonPath("$[1].duration").value(1.0))
                .andExpect(jsonPath("$[1].halfDay", nullValue()))
                .andExpect(jsonPath("$[1].description").value("Vacation"));
    }

    @Test
    public void Should_ReturnStartDateRequired_When_StartDateIsNotGiven() throws Exception {
        Long userId = 1L;
        LeaveDTO leaveDTO = LeaveDTO.builder()
                .userId(userId)
                .endDate(LocalDate.of(2024, 3, 17))
                .description("Vacation")
                .leaveType(String.valueOf(LeaveType.FULLDAY))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody = objectMapper.writeValueAsString(leaveDTO);

        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(userId.toString());

        mockMvc = MockMvcBuilders.standaloneSetup(leaveController)
                        .setControllerAdvice(new ValidationHandler()) 
                        .build();

        mockMvc.perform(post("/v1/oauth/leaves")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(jsonPath("$.startDate").value("Start Date is required"));
    }

    @Test
    public void Should_ReturnEndDateRequired_When_EndDateIsNotGiven() throws Exception {
        LeaveDTO leaveDTO = LeaveDTO.builder()
                .startDate(LocalDate.of(2024, 3, 16))
                .description("Vacation")
                .userId(1L)
                .leaveType(String.valueOf(LeaveType.FULLDAY))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody = objectMapper.writeValueAsString(leaveDTO);

        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(leaveDTO.getUserId().toString());

        mockMvc = MockMvcBuilders.standaloneSetup(leaveController)
                        .setControllerAdvice(new ValidationHandler()) 
                        .build();

        mockMvc.perform(post("/v1/oauth/leaves")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(jsonPath("$.endDate").value("End Date is required"));
    }

    @Test
    public void Should_ReturnLeaveTypeRequired_When_LeaveTypeIsNotGiven() throws Exception {
        LeaveDTO leaveDTO = LeaveDTO.builder()
                .startDate(LocalDate.of(2024, 3, 16))
                .endDate(LocalDate.of(2024, 3, 17))
                .description("Vacation")
                .userId(1L)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody = objectMapper.writeValueAsString(leaveDTO);

        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(leaveDTO.getUserId().toString());

        mockMvc = MockMvcBuilders.standaloneSetup(leaveController)
                        .setControllerAdvice(new ValidationHandler()) 
                        .build();

        mockMvc.perform(post("/v1/oauth/leaves")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(jsonPath("$.leaveType").value("Leave Type is required"));
    }

    @Test
    public void Should_ReturnDescriptionRequired_When_DescriptionIsNotGiven() throws Exception {
        LeaveDTO leaveDTO = LeaveDTO.builder()
                .startDate(LocalDate.of(2024, 3, 16))
                .endDate(LocalDate.of(2024, 3, 17))
                .userId(1L)
                .leaveType(String.valueOf(LeaveType.FULLDAY))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody = objectMapper.writeValueAsString(leaveDTO);

        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(leaveDTO.getUserId().toString());

        mockMvc = MockMvcBuilders.standaloneSetup(leaveController)
                        .setControllerAdvice(new ValidationHandler()) 
                        .build();

        mockMvc.perform(post("/v1/oauth/leaves")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(jsonPath("$.description").value("Description is required"));
    }
}
