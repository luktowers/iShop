package com.client.controllers;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(value = HealthCheckController.class)
@AutoConfigureMockMvc(addFilters = false)
@RequiredArgsConstructor
public class HealthCheckControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void status() throws Exception {
        var response = mvc.perform(
                get("/check/"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("ok", response.getContentAsString());
    }
}
