package com.technogise.leavemanagementbe.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest
public class GreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private GreetingController greetingController;

    @Test
    public void Should_ReturnGreetingMessage_When_EndpointIsAccessed() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello World !"));
    }
}
