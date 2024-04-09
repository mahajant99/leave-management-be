package com.technogise.leavemanagement.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @DisplayName("Given users exist, when retrieving all users with pagination, then it should succeed")
    public void testRetrieveAllUsersSuccess() throws Exception {
        int page = 0;
        int size = 6;

        User user = new User();
        user.setId(1L);
        Page<User> mockPage = new PageImpl<>(List.of(user), PageRequest.of(page, size), 1);

        when(userService.getAllUsers(anyInt(), anyInt())).thenReturn(mockPage);

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(get("/v1/oauth/users")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("Given a user exists, when retrieving user info by ID, then it should return the user's details")
    public void testGetUserInfo() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Rick");

        Mockito.when(userService.getUser(1L)).thenReturn(user);

        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(user.getId().toString());

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(get("/v1/oauth/user/info")
                .principal(principal)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rick"));
    }
}
