package com.example.bookingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AdminEndpointsSecurityIT {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    String userToken;

    @BeforeEach
    void init() throws Exception {
        var reg = mvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("username", "usr", "password", "pwd"))))
                .andReturn();
        userToken = om.readTree(reg.getResponse().getContentAsString()).get("token").asText();
    }

    @Test
    void userCannotAccessAdminCreateUser() throws Exception {
        mvc.perform(post("/user")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of(
                                "username", "x",
                                "password", "y",
                                "role", "ADMIN"))))
                .andExpect(status().isForbidden());
    }
}