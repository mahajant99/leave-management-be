package com.technogise.leavemanagement.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HealthController.class)
public class HealthControllerTest {
    @InjectMocks
    private HealthController healthController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void Should_ReturnOkStatus_When_EndpointIsAccessed() throws Exception {
        mockMvc.perform(get("/health-check"))
                .andExpect(status().isOk());
    }
}
