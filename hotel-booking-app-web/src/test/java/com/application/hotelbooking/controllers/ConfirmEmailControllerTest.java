package com.application.hotelbooking.controllers;

import com.application.hotelbooking.exceptions.EmailAlreadyConfirmedException;
import com.application.hotelbooking.exceptions.ExpiredTokenException;
import com.application.hotelbooking.exceptions.InvalidTokenException;
import com.application.hotelbooking.exceptions.InvalidUserException;
import com.application.hotelbooking.security.SecurityConfiguration;
import com.application.hotelbooking.services.ResendConfirmationTokenService;
import com.application.hotelbooking.services.UserEmailTokenConfirmationService;
import com.application.hotelbooking.services.imagehandling.FileSystemStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@Import({SecurityConfiguration.class, FileSystemStorageService.class})
@WebMvcTest(ConfirmEmailController.class)
public class ConfirmEmailControllerTest {

    private static final String EMAIL = "test@email.com";
    private static final String CONFIRMATION_TOKEN = "confirmation-token";
    @MockBean
    private UserEmailTokenConfirmationService userEmailTokenConfirmationService;

    @MockBean
    private ResendConfirmationTokenService resendConfirmationTokenService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUnauthenticatedUserCanNavigateToConfirmEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/register/confirmemail")
                        .sessionAttr("email", EMAIL))
                .andExpect(status().isOk())
                .andExpect(view().name("confirmemail"))
                .andExpect(model().attribute("email", EMAIL));
    }

    @Test
    public void testConfirmTokenShouldRedirectToConfirmEmailPageWithInvalidLinkErrorIfConfirmTokenThrowsInvalidTokenException() throws Exception {
        doThrow(InvalidTokenException.class).when(userEmailTokenConfirmationService).confirmToken(CONFIRMATION_TOKEN);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/register/confirmemail/confirm-token")
                        .param("confirmationToken", CONFIRMATION_TOKEN))
                .andExpect(redirectedUrl("/hotelbooking/register/confirmemail?invalidLink"));

        verify(userEmailTokenConfirmationService).confirmToken(CONFIRMATION_TOKEN);
    }

    @Test
    public void testConfirmTokenShouldRedirectToConfirmEmailPageWithEmailAlreadyConfirmedErrorIfConfirmTokenThrowsEmailAlreadyConfirmedException() throws Exception {
        doThrow(EmailAlreadyConfirmedException.class).when(userEmailTokenConfirmationService).confirmToken(CONFIRMATION_TOKEN);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/register/confirmemail/confirm-token")
                        .param("confirmationToken", CONFIRMATION_TOKEN))
                .andExpect(redirectedUrl("/hotelbooking/register/confirmemail?emailAlreadyConfirmed"));

        verify(userEmailTokenConfirmationService).confirmToken(CONFIRMATION_TOKEN);
    }

    @Test
    public void testConfirmTokenShouldRedirectToConfirmEmailPageWithTokenAlreadyExpiredErrorIfConfirmTokenThrowsExpiredTokenException() throws Exception {
        doThrow(ExpiredTokenException.class).when(userEmailTokenConfirmationService).confirmToken(CONFIRMATION_TOKEN);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/register/confirmemail/confirm-token")
                        .param("confirmationToken", CONFIRMATION_TOKEN))
                .andExpect(redirectedUrl("/hotelbooking/register/confirmemail?tokenAlreadyExpired"));

        verify(userEmailTokenConfirmationService).confirmToken(CONFIRMATION_TOKEN);
    }

    @Test
    public void testConfirmTokenShouldRedirectToConfirmEmailPageWithGenericErrorIfConfirmTokenThrowsAnyOtherException() throws Exception {
        doThrow(DataIntegrityViolationException.class).when(userEmailTokenConfirmationService).confirmToken(CONFIRMATION_TOKEN);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/register/confirmemail/confirm-token")
                        .param("confirmationToken", CONFIRMATION_TOKEN))
                .andExpect(redirectedUrl("/hotelbooking/register/confirmemail?error"));

        verify(userEmailTokenConfirmationService).confirmToken(CONFIRMATION_TOKEN);
    }

    @Test
    public void testConfirmTokenShouldRedirectToLoginPageIfNoExceptionsOccurredDuringTokenConfirmation() throws Exception {
        doNothing().when(userEmailTokenConfirmationService).confirmToken(CONFIRMATION_TOKEN);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/register/confirmemail/confirm-token")
                        .param("confirmationToken", CONFIRMATION_TOKEN))
                .andExpect(redirectedUrl("/hotelbooking/login"));

        verify(userEmailTokenConfirmationService).confirmToken(CONFIRMATION_TOKEN);
    }

    @Test
    public void testSendNewTokenShouldRedirectToConfirmEmailPageWithInvalidUserErrorIfResendConfirmationTokenThrowsInvalidUserException() throws Exception {
        doThrow(InvalidUserException.class).when(resendConfirmationTokenService).resendConfirmationToken(EMAIL);

        mockMvc.perform(MockMvcRequestBuilders.post("/hotelbooking/register/confirmemail/send-new-token")
                        .sessionAttr("email", EMAIL))
                .andExpect(redirectedUrl("/hotelbooking/confirmemail?invalidUser"));

        verify(resendConfirmationTokenService).resendConfirmationToken(EMAIL);
    }

    @Test
    public void testSendNewTokenShouldRedirectToConfirmEmailPageWithEmailAlreadyConfirmedErrorIfResendConfirmationTokenThrowsEmailAlreadyConfirmedException() throws Exception {
        doThrow(EmailAlreadyConfirmedException.class).when(resendConfirmationTokenService).resendConfirmationToken(EMAIL);

        mockMvc.perform(MockMvcRequestBuilders.post("/hotelbooking/register/confirmemail/send-new-token")
                        .sessionAttr("email", EMAIL))
                .andExpect(redirectedUrl("/hotelbooking/confirmemail?emailAlreadyConfirmed"));

        verify(resendConfirmationTokenService).resendConfirmationToken(EMAIL);
    }

    @Test
    public void testSendNewTokenShouldRedirectToConfirmEmailPageWithResendErrorIfResendConfirmationTokenThrowsAnyOtherException() throws Exception {
        doThrow(DataIntegrityViolationException.class).when(resendConfirmationTokenService).resendConfirmationToken(EMAIL);

        mockMvc.perform(MockMvcRequestBuilders.post("/hotelbooking/register/confirmemail/send-new-token")
                        .sessionAttr("email", EMAIL))
                .andExpect(redirectedUrl("/hotelbooking/confirmemail?resendError"));

        verify(resendConfirmationTokenService).resendConfirmationToken(EMAIL);
    }

    @Test
    public void testConfirmTokenShouldReturnToConfirmEmailPageIfNoExceptionsOccurredWhileResendingToken() throws Exception {
        doNothing().when(resendConfirmationTokenService).resendConfirmationToken(EMAIL);

        mockMvc.perform(MockMvcRequestBuilders.post("/hotelbooking/register/confirmemail/send-new-token")
                        .sessionAttr("email", EMAIL))
                .andExpect(view().name("confirmemail"));

        verify(resendConfirmationTokenService).resendConfirmationToken(EMAIL);
    }
}
