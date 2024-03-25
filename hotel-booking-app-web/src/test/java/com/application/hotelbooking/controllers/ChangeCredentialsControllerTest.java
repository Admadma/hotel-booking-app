package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.ChangeCredentialsDto;
import com.application.hotelbooking.exceptions.CredentialMismatchException;
import com.application.hotelbooking.exceptions.InvalidUserException;
import com.application.hotelbooking.security.SecurityConfiguration;
import com.application.hotelbooking.services.UserService;
import com.application.hotelbooking.services.imagehandling.FileSystemStorageService;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({SecurityConfiguration.class, FileSystemStorageService.class})
@WebMvcTest(ChangeCredentialsController.class)
public class ChangeCredentialsControllerTest {

    private static final ChangeCredentialsDto CHANGE_CREDENTIALS_DTO = new ChangeCredentialsDto("12345678", "123345678");
    private static final ChangeCredentialsDto EMPTY_CHANGE_CREDENTIALS_DTO = new ChangeCredentialsDto();
    private static final ChangeCredentialsDto CHANGE_CREDENTIALS_DTO_WITH_TWO_INVALID_FIELDS = new ChangeCredentialsDto("1", "1");
    public static final String TEST_USER_NAME = "test_user";
    @MockBean
    private UserService userService;;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "USER")
    public void testUserCanNavigateToAccountPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/account"))
                .andExpect(status().isOk())
                .andExpect(view().name("account"))
                .andExpect(model().attribute("credentials", EMPTY_CHANGE_CREDENTIALS_DTO));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testAdminCanNavigateToAccountPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/account"))
                .andExpect(status().isOk())
                .andExpect(view().name("account"))
                .andExpect(model().attribute("credentials", EMPTY_CHANGE_CREDENTIALS_DTO));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testAdminUserCanAttemptToChangePassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/change-password")
                        .flashAttr("credentials", CHANGE_CREDENTIALS_DTO_WITH_TWO_INVALID_FIELDS))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testChangePasswordShouldReturnToAccountPageWithErrorIfBindingResultHasErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/change-password")
                        .flashAttr("credentials", CHANGE_CREDENTIALS_DTO_WITH_TWO_INVALID_FIELDS))
                .andExpect(status().isOk())
                .andExpect(view().name("account"))
                .andExpect(model().attribute("credentials", CHANGE_CREDENTIALS_DTO_WITH_TWO_INVALID_FIELDS))
                .andExpect(model().attributeErrorCount("credentials", 2));
    }

    @Test
    @WithMockUser(username = TEST_USER_NAME, authorities = "USER")
    public void testChangePasswordShouldRedirectToAccountPageWithInvalidUserErrorIfChangePasswordThrowsInvalidUserException() throws Exception {
        doThrow(InvalidUserException.class).when(userService).changePassword(TEST_USER_NAME, CHANGE_CREDENTIALS_DTO.getNewPassword(), CHANGE_CREDENTIALS_DTO.getOldPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/hotelbooking/change-password")
                        .flashAttr("credentials", CHANGE_CREDENTIALS_DTO))
                .andExpect(redirectedUrl("/hotelbooking/account?invalidUserError"));

        verify(userService).changePassword(TEST_USER_NAME, CHANGE_CREDENTIALS_DTO.getNewPassword(), CHANGE_CREDENTIALS_DTO.getOldPassword());
    }

    @Test
    @WithMockUser(username = TEST_USER_NAME, authorities = "USER")
    public void testChangePasswordShouldRedirectToAccountPageWithGenericErrorIfChangePasswordThrowsOptimisticLockException() throws Exception {
        doThrow(OptimisticLockException.class).when(userService).changePassword(TEST_USER_NAME, CHANGE_CREDENTIALS_DTO.getNewPassword(), CHANGE_CREDENTIALS_DTO.getOldPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/hotelbooking/change-password")
                        .flashAttr("credentials", CHANGE_CREDENTIALS_DTO))
                .andExpect(redirectedUrl("/hotelbooking/account?error"));

        verify(userService).changePassword(TEST_USER_NAME, CHANGE_CREDENTIALS_DTO.getNewPassword(), CHANGE_CREDENTIALS_DTO.getOldPassword());
    }

    @Test
    @WithMockUser(username = TEST_USER_NAME, authorities = "USER")
    public void testChangePasswordShouldRejectOldPasswordAndReturnToAccountPageIfChangePasswordThrowsCredentialMismatchException() throws Exception {
        doThrow(CredentialMismatchException.class).when(userService).changePassword(TEST_USER_NAME, CHANGE_CREDENTIALS_DTO.getNewPassword(), CHANGE_CREDENTIALS_DTO.getOldPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/hotelbooking/change-password")
                        .flashAttr("credentials", CHANGE_CREDENTIALS_DTO))
                .andExpect(view().name("account"))
                .andExpect(model().attributeHasFieldErrorCode("credentials", "oldPassword", "account.form.old.password.not.found"));

        verify(userService).changePassword(TEST_USER_NAME, CHANGE_CREDENTIALS_DTO.getNewPassword(), CHANGE_CREDENTIALS_DTO.getOldPassword());
    }

    @Test
    @WithMockUser(username = TEST_USER_NAME, authorities = "USER")
    public void testChangePasswordShouldRedirectToAccountPageWithGenericErrorIfChangePasswordThrowsAnyOtherException() throws Exception {
        doThrow(DataIntegrityViolationException.class).when(userService).changePassword(TEST_USER_NAME, CHANGE_CREDENTIALS_DTO.getNewPassword(), CHANGE_CREDENTIALS_DTO.getOldPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/hotelbooking/change-password")
                        .flashAttr("credentials", CHANGE_CREDENTIALS_DTO))
                .andExpect(redirectedUrl("/hotelbooking/account?error"));

        verify(userService).changePassword(TEST_USER_NAME, CHANGE_CREDENTIALS_DTO.getNewPassword(), CHANGE_CREDENTIALS_DTO.getOldPassword());
    }

    @Test
    @WithMockUser(username = TEST_USER_NAME, authorities = "USER")
    public void testChangePasswordShouldRedirectToAccountPageWithSuccessMessageIfNoExceptionsOccurredWhileChangingPassword() throws Exception {
        doNothing().when(userService).changePassword(TEST_USER_NAME, CHANGE_CREDENTIALS_DTO.getNewPassword(), CHANGE_CREDENTIALS_DTO.getOldPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/hotelbooking/change-password")
                        .flashAttr("credentials", CHANGE_CREDENTIALS_DTO))
                .andExpect(redirectedUrl("/hotelbooking/account?success"));

        verify(userService).changePassword(TEST_USER_NAME, CHANGE_CREDENTIALS_DTO.getNewPassword(), CHANGE_CREDENTIALS_DTO.getOldPassword());
    }
}
