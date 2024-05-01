package com.application.hotelbooking.services;

import com.application.hotelbooking.models.ConfirmationTokenModel;
import com.application.hotelbooking.models.UserModel;
import com.application.hotelbooking.exceptions.EmailAlreadyConfirmedException;
import com.application.hotelbooking.exceptions.ExpiredTokenException;
import com.application.hotelbooking.exceptions.InvalidTokenException;
import com.application.hotelbooking.services.implementations.UserEmailTokenConfirmationServiceImpl;
import com.application.hotelbooking.services.repositoryservices.ConfirmationTokenRepositoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserEmailTokenConfirmationServiceImplTest {

    private static final String TOKEN = "test_token";
    private static final String USERNAME = "test_username";
    private static final String EMAIL = "test_email";
    private static final UserModel USER_MODEL_ENABLED = UserModel.builder().username(USERNAME).email(EMAIL).enabled(true).build();
    private static final ConfirmationTokenModel CONFIRMATION_TOKEN_MODEL = ConfirmationTokenModel.builder().user(USER_MODEL_ENABLED).expiresAt(LocalDateTime.now().plusDays(10)).build();
    private static final Optional<ConfirmationTokenModel> CONFIRMATION_TOKEN_MODEL_CONFIRMED = Optional.of(ConfirmationTokenModel.builder().token(TOKEN).confirmedAt(LocalDateTime.now()).build());
    private static final Optional<ConfirmationTokenModel> CONFIRMATION_TOKEN_MODEL_EXPIRED = Optional.of(ConfirmationTokenModel.builder().token(TOKEN).expiresAt(LocalDateTime.now().minusDays(10)).build());

    @InjectMocks
    private UserEmailTokenConfirmationServiceImpl userEmailTokenConfirmationService;

    @Mock
    private ConfirmationTokenRepositoryService confirmationTokenRepositoryService;

    @Mock
    private UserService userService;

    @Test
    public void testConfirmTokenShouldThrowInvalidTokenExceptionIfConfirmationTokenNotFound(){
        when(confirmationTokenRepositoryService.getByToken(TOKEN)).thenThrow(new InvalidTokenException("Confirmation token not found."));

        assertThrows(InvalidTokenException.class,
                () -> userEmailTokenConfirmationService.confirmToken(TOKEN));
    }

    @Test
    public void testConfirmTokenShouldThrowEmailAlreadyConfirmedExceptionIfEmailIsAlreadyConfirmed(){
        when(confirmationTokenRepositoryService.getByToken(TOKEN)).thenReturn(CONFIRMATION_TOKEN_MODEL_CONFIRMED);

        assertThrows(EmailAlreadyConfirmedException.class,
                () -> userEmailTokenConfirmationService.confirmToken(TOKEN));
    }

    @Test
    public void testConfirmTokenShouldThrowExpiredTokenExceptionIfTokenExistsAndNotConfirmedAndExpired(){
        when(confirmationTokenRepositoryService.getByToken(TOKEN)).thenReturn(CONFIRMATION_TOKEN_MODEL_EXPIRED);

        assertThrows(ExpiredTokenException.class,
                () -> userEmailTokenConfirmationService.confirmToken(TOKEN));
    }

    @Test
    public void testConfirmTokenShouldEnableUserIfTokenExistsAndUnconfirmedAndNotExpired(){
        when(confirmationTokenRepositoryService.getByToken(TOKEN)).thenReturn(Optional.of(CONFIRMATION_TOKEN_MODEL));
        when(confirmationTokenRepositoryService.saveConfirmationToken(any(ConfirmationTokenModel.class))).thenReturn(CONFIRMATION_TOKEN_MODEL);
        when(userService.enableUser(EMAIL)).thenReturn(USER_MODEL_ENABLED);

        userEmailTokenConfirmationService.confirmToken(TOKEN);

        verify(confirmationTokenRepositoryService).getByToken(TOKEN);
        verify(confirmationTokenRepositoryService).saveConfirmationToken(any(ConfirmationTokenModel.class));
        verify(userService).enableUser(EMAIL);
    }
}
