package com.application.hotelbooking.controllers;

import com.application.hotelbooking.security.SecurityConfiguration;
import com.application.hotelbooking.services.UserService;
import com.application.hotelbooking.services.imagehandling.FileSystemStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({SecurityConfiguration.class, FileSystemStorageService.class})
@WebMvcTest(DefaultSuccessLoginController.class)
public class DefaultSuccessLoginControllerTest {

    private static final String TEST_USER_NAME = "test_user";
    private static final String ADMIN_ROLE_NAME = "ADMIN";

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = TEST_USER_NAME)
    public void testDefaultPageShouldRedirectToAdminPageIfLoggedInUserIsAdmin() throws Exception {
        when(userService.userHasRole(TEST_USER_NAME, ADMIN_ROLE_NAME)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/default"))
                .andExpect(redirectedUrl("/hotelbooking/admin"));

        verify(userService).userHasRole(TEST_USER_NAME, ADMIN_ROLE_NAME);
    }

    @Test
    @WithMockUser(username = TEST_USER_NAME)
    public void testDefaultPageShouldRedirectToHomePageIfLoggedInUserIsNotAdmin() throws Exception {
        when(userService.userHasRole(TEST_USER_NAME, ADMIN_ROLE_NAME)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/default"))
                .andExpect(redirectedUrl("/hotelbooking/home"));

        verify(userService).userHasRole(TEST_USER_NAME, ADMIN_ROLE_NAME);
    }
}
