package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.ConfirmationTokenModel;
import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.exceptions.EmailAlreadyConfirmedException;
import com.application.hotelbooking.exceptions.ExpiredTokenException;
import com.application.hotelbooking.exceptions.InvalidTokenException;
import com.application.hotelbooking.services.implementations.UserEmailConfirmationServiceImpl;
import com.application.hotelbooking.services.repositoryservices.ConfirmationTokenRepositoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserEmailConfirmationServiceImplTest {

    private static final String TOKEN = "test_token";
    private static final String USERNAME = "test_username";
    private static final String EMAIL = "test_email";
    private static final String SUBJECT_CODE = "email.confirmation.link.subject";
    private static final String SUBJECT = "test_subject";
    private static final String BODY_CODE = "email.confirmation.link.body";
    private static final String BODY = "test_body";
    private static final String CONFIRM_TEXT_CODE = "email.confirmation.link.confirm";
    private static final String CONFIRM_TEXT = "test_confirm_text";
    private static final UserModel USER_MODEL_ENABLED = UserModel.builder().username(USERNAME).email(EMAIL).enabled(true).build();
    private static final ConfirmationTokenModel CONFIRMATION_TOKEN_MODEL = ConfirmationTokenModel.builder().user(USER_MODEL_ENABLED).expiresAt(LocalDateTime.now().plusDays(10)).build();
    private static final Optional<ConfirmationTokenModel> CONFIRMATION_TOKEN_MODEL_CONFIRMED = Optional.of(ConfirmationTokenModel.builder().token(TOKEN).confirmedAt(LocalDateTime.now()).build());
    private static final Optional<ConfirmationTokenModel> CONFIRMATION_TOKEN_MODEL_EXPIRED = Optional.of(ConfirmationTokenModel.builder().token(TOKEN).expiresAt(LocalDateTime.now().minusDays(10)).build());
    private static final Optional<UserModel> OPTIONAL_USER_MODEL = Optional.of(UserModel.builder().username(USERNAME).enabled(true).build());
    private static final Optional<UserModel> OPTIONAL_USER_MODEL_NOT_ENABLED = Optional.of(UserModel.builder().username(USERNAME).enabled(false).build());

    @InjectMocks
    private UserEmailConfirmationServiceImpl userEmailConfirmationServiceImpl;

    @Mock
    private ConfirmationTokenRepositoryService confirmationTokenRepositoryService;

    @Mock
    private UserService userService;

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private MessageSource messageSource;

    @Test
    public void testSendConfirmationTokenShouldBuildNewConfirmationTokenAndSaveItAndSendItInEmail(){
        when(confirmationTokenRepositoryService.saveConfirmationToken(any(ConfirmationTokenModel.class))).thenReturn(CONFIRMATION_TOKEN_MODEL);
        when(messageSource.getMessage(eq(BODY_CODE), eq(null), any(Locale.class))).thenReturn(BODY);
        when(messageSource.getMessage(eq(CONFIRM_TEXT_CODE), eq(null), any(Locale.class))).thenReturn(CONFIRM_TEXT);
        when(messageSource.getMessage(eq(SUBJECT_CODE), eq(null), any(Locale.class))).thenReturn(SUBJECT);
        doNothing().when(emailSenderService).sendEmail(eq(EMAIL), eq(SUBJECT), any(String.class));

        userEmailConfirmationServiceImpl.sendConfirmationToken(USER_MODEL_ENABLED);

        verify(confirmationTokenRepositoryService).saveConfirmationToken(any(ConfirmationTokenModel.class));
        verify(messageSource).getMessage(eq(BODY_CODE), eq(null), any(Locale.class));
        verify(messageSource).getMessage(eq(CONFIRM_TEXT_CODE), eq(null), any(Locale.class));
        verify(messageSource).getMessage(eq(SUBJECT_CODE), eq(null), any(Locale.class));
        verify(emailSenderService).sendEmail(eq(EMAIL), eq(SUBJECT), any(String.class));
    }

    @Test
    public void testConfirmTokenShouldThrowInvalidTokenExceptionIfConfirmationTokenNotFound(){
        when(confirmationTokenRepositoryService.getByToken(TOKEN)).thenThrow(new InvalidTokenException("Confirmation token not found."));

        assertThrows(InvalidTokenException.class,
                () -> userEmailConfirmationServiceImpl.confirmToken(TOKEN));
    }

    @Test
    public void testConfirmTokenShouldThrowEmailAlreadyConfirmedExceptionIfEmailIsAlreadyConfirmed(){
        when(confirmationTokenRepositoryService.getByToken(TOKEN)).thenReturn(CONFIRMATION_TOKEN_MODEL_CONFIRMED);

        assertThrows(EmailAlreadyConfirmedException.class,
                () -> userEmailConfirmationServiceImpl.confirmToken(TOKEN));
    }

    @Test
    public void testConfirmTokenShouldThrowExpiredTokenExceptionIfTokenExistsAndNotConfirmedAndExpired(){
        when(confirmationTokenRepositoryService.getByToken(TOKEN)).thenReturn(CONFIRMATION_TOKEN_MODEL_EXPIRED);

        assertThrows(ExpiredTokenException.class,
                () -> userEmailConfirmationServiceImpl.confirmToken(TOKEN));
    }

    @Test
    public void testConfirmTokenShouldEnableUserIfTokenExistsAndUnconfirmedAndNotExpired(){
        when(confirmationTokenRepositoryService.getByToken(TOKEN)).thenReturn(Optional.of(CONFIRMATION_TOKEN_MODEL));
        when(confirmationTokenRepositoryService.saveConfirmationToken(any(ConfirmationTokenModel.class))).thenReturn(CONFIRMATION_TOKEN_MODEL);
        when(userService.enableUser(EMAIL)).thenReturn(USER_MODEL_ENABLED);

        userEmailConfirmationServiceImpl.confirmToken(TOKEN);

        verify(confirmationTokenRepositoryService).getByToken(TOKEN);
        verify(confirmationTokenRepositoryService).saveConfirmationToken(any(ConfirmationTokenModel.class));
        verify(userService).enableUser(EMAIL);
    }

}
