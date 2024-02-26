package com.technogise.leavemanagement.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest
public class HealthContollerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private HealthContollerTest healthContollerTest;

    @Test
    public void Should_ReturnOkStatus_When_EndpointIsAccessed() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/health-check"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
