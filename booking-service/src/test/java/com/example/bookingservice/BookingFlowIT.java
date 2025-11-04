package com.example.bookingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BookingFlowIT {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @Test
    void fullAuthAndBookingFlow() throws Exception {
        var registerJson = om.writeValueAsString(Map.of(
                "username", "user1",
                "password", "pass1"
        ));
        var reg = mvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isOk())
                .andReturn();

        var regToken = om.readTree(reg.getResponse().getContentAsString()).get("token").asText();
        assertThat(regToken).isNotBlank();

        var authJson = om.writeValueAsString(Map.of(
                "username", "user1",
                "password", "pass1"
        ));
        var auth = mvc.perform(post("/user/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authJson))
                .andExpect(status().isOk())
                .andReturn();

        var authToken = om.readTree(auth.getResponse().getContentAsString()).get("token").asText();
        assertThat(authToken).isNotBlank();

        var today = LocalDate.now();
        var bookingJson = om.writeValueAsString(Map.of(
                "autoSelect", true,
                "startDate", today.plusDays(2).toString(),
                "endDate", today.plusDays(5).toString()
        ));

        var created = mvc.perform(post("/booking")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson))
                .andExpect(status().isCreated())
                .andReturn();

        var bookingId = om.readTree(created.getResponse().getContentAsString()).get("id").asLong();
        assertThat(bookingId).isPositive();

        mvc.perform(get("/bookings")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingId));
    }
}