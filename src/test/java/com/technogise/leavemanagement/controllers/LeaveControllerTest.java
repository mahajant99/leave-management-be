package com.technogise.leavemanagement.controllers;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
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

@ExtendWith(MockitoExtension.class)
public class LeaveControllerTest {

    @Mock
    private LeaveService leaveService;

    @InjectMocks
    private LeaveController leaveController;

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
    public void givenLeaveId_whenDeleteLeave_thenStatusNoContent() throws Exception {
        Long id = 1L;
        doNothing().when(leaveService).remove(id);

        mockMvc.perform(delete("/leaves/{id}", id))
                .andExpect(status().isNoContent());
    }

}
