package com.application.hotelbooking.controllers;

import com.application.hotelbooking.security.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfiguration.class)
@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testAuthenticatedUserShouldBeRedirectedToHomePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/login"))
                .andExpect(redirectedUrl("/hotelbooking/home"));
    }

    @Test
    public void testUnauthenticatedUserShouldBeSentToLoginPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/login"))
                .andExpect(view().name("login"));
    }
}
