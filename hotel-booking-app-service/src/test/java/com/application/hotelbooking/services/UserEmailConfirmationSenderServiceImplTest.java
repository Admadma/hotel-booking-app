package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.ConfirmationTokenModel;
import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.services.implementations.UserEmailConfirmationSenderServiceImpl;
import com.application.hotelbooking.services.repositoryservices.ConfirmationTokenRepositoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserEmailConfirmationSenderServiceImplTest {

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

    @InjectMocks
    private UserEmailConfirmationSenderServiceImpl userEmailConfirmationSenderServiceImpl;

    @Mock
    private ConfirmationTokenRepositoryService confirmationTokenRepositoryService;

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

        userEmailConfirmationSenderServiceImpl.sendConfirmationToken(USER_MODEL_ENABLED);

        verify(confirmationTokenRepositoryService).saveConfirmationToken(any(ConfirmationTokenModel.class));
        verify(messageSource).getMessage(eq(BODY_CODE), eq(null), any(Locale.class));
        verify(messageSource).getMessage(eq(CONFIRM_TEXT_CODE), eq(null), any(Locale.class));
        verify(messageSource).getMessage(eq(SUBJECT_CODE), eq(null), any(Locale.class));
        verify(emailSenderService).sendEmail(eq(EMAIL), eq(SUBJECT), any(String.class));
    }
}
