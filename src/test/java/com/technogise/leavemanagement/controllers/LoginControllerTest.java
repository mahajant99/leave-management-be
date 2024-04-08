package com.technogise.leavemanagement.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.technogise.leavemanagement.dtos.IdTokenRequestDto;
import com.technogise.leavemanagement.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    @Test
    @DisplayName("Given a valid Google OAuth2 token, when logging in with Google OAuth2, then the expected authentication token is returned and a cookie is set")
    public void testLoginWithGoogleOauth2() throws Exception {
    
        String authToken = "testAuthToken";
        when(userService.loginOAuthGoogle(any(IdTokenRequestDto.class))).thenReturn(authToken);

        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/oauth/login")
                .contentType("application/json")
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    String cookieHeader = response.getHeader(HttpHeaders.SET_COOKIE);
                    assertNotNull(cookieHeader);
                    assertTrue(cookieHeader.contains("AUTH-TOKEN=" + authToken));
                });
    }
    
}
