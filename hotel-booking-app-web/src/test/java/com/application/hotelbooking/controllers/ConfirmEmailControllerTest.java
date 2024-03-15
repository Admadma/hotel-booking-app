package com.application.hotelbooking.controllers;

import com.application.hotelbooking.services.ResendConfirmationTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ConfirmEmailController.class)
public class ConfirmEmailControllerTest {

//    @MockBean
//    private UserEmailConfirmationService userEmailConfirmationService;

    @MockBean
    private ResendConfirmationTokenService resendConfirmationTokenService;

    @Autowired
    private MockMvc mockMvc;
}
