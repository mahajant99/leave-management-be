package com.technogise.leavemanagement.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class HealthControllerTest {
    @InjectMocks
    private HealthController healthController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void Should_ReturnOkStatus_When_EndpointIsAccessed() throws Exception {
        
        mockMvc = MockMvcBuilders.standaloneSetup(healthController).build();
        
        mockMvc.perform(get("/health-check"))
                .andExpect(status().isOk());
    }
}
