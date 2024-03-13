package com.application.hotelbooking.controllers;

import com.application.hotelbooking.services.ReservationService;
import com.application.hotelbooking.transformers.ReservationViewTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@WebMvcTest(MyReservationsController.class)
public class MyReservationsControllerTest {

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private ReservationViewTransformer reservationViewTransformer;

    @Autowired
    private MockMvc mockMvc;
    /*
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setupMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }
    */

    @Test
    void shouldCreateMockMVC(){
        assertNotNull(mockMvc);
    }
}
