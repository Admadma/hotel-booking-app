package com.application.hotelbooking.services;

import com.application.hotelbooking.models.UserModel;
import com.application.hotelbooking.exceptions.EmailAlreadyConfirmedException;
import com.application.hotelbooking.exceptions.InvalidUserException;
import com.application.hotelbooking.services.implementations.ResendConfirmationTokenServiceImpl;
import com.application.hotelbooking.services.repositoryservices.UserRepositoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResendConfirmationTokenServiceImplTest {

    private static final String USERNAME = "test_username";
    private static final String EMAIL = "test_email";
    private static final UserModel USER_MODEL_NOT_ENABLED = UserModel.builder().username(USERNAME).email(EMAIL).enabled(false).build();
    private static final Optional<UserModel> OPTIONAL_USER_MODEL = Optional.of(UserModel.builder().username(USERNAME).email(EMAIL).enabled(true).build());
    private static final Optional<UserModel> OPTIONAL_USER_MODEL_NOT_ENABLED = Optional.of(UserModel.builder().username(USERNAME).email(EMAIL).enabled(false).build());

    @InjectMocks
    private ResendConfirmationTokenServiceImpl resendConfirmationTokenService;
    @Mock
    private UserRepositoryService userRepositoryService;
    @Mock
    private UserEmailConfirmationSenderService userEmailConfirmationService;

    @Test
    public void testResendConfirmationTokenShouldThrowInvalidUserExceptionIfEmailDoesNotExist(){
        when(userRepositoryService.emailExists(EMAIL)).thenReturn(false);

        assertThrows(InvalidUserException.class,
                () -> resendConfirmationTokenService.resendConfirmationToken(EMAIL));

        verify(userRepositoryService).emailExists(EMAIL);
    }

    @Test
    public void testResendConfirmationTokenShouldThrowEmailAlreadyConfirmedExceptionIfEmailExistsAndWasAlreadyConfirmed(){
        when(userRepositoryService.emailExists(EMAIL)).thenReturn(true);
        when(userRepositoryService.getUserByEmail(EMAIL)).thenReturn(OPTIONAL_USER_MODEL);

        assertThrows(EmailAlreadyConfirmedException.class,
                () -> resendConfirmationTokenService.resendConfirmationToken(EMAIL));

        verify(userRepositoryService).emailExists(EMAIL);
        verify(userRepositoryService).getUserByEmail(EMAIL);
    }

    @Test
    public void testResendConfirmationTokenShouldCallSendConfirmationTokenIfEmailExistsAndNotYetEnabled(){
        when(userRepositoryService.emailExists(EMAIL)).thenReturn(true);
        when(userRepositoryService.getUserByEmail(EMAIL))
                .thenReturn(OPTIONAL_USER_MODEL_NOT_ENABLED)
                .thenReturn(OPTIONAL_USER_MODEL_NOT_ENABLED);
        doNothing().when(userEmailConfirmationService).sendConfirmationToken(USER_MODEL_NOT_ENABLED);

        resendConfirmationTokenService.resendConfirmationToken(EMAIL);

        verify(userRepositoryService).emailExists(EMAIL);
        verify(userRepositoryService, times(2)).getUserByEmail(EMAIL);
        verify(userEmailConfirmationService).sendConfirmationToken(USER_MODEL_NOT_ENABLED);
    }
}
