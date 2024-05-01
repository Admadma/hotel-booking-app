package com.application.hotelbooking.services;

import com.application.hotelbooking.factories.MimeMessageFactory;
import com.application.hotelbooking.services.implementations.EmailSenderServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailSenderServiceImplTest {

    @InjectMocks
    private EmailSenderServiceImpl emailSenderService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessageFactory mimeMessageFactory;

    @Mock
    private MimeMessage mockMimeMessage;

    @Test
    public void testSendEmailShouldSendCreatedMessageIfNoException() throws MessagingException {
        when(mimeMessageFactory.createMimeMessage("to@example.com", "Subject", "Body")).thenReturn(mockMimeMessage);

        emailSenderService.sendEmail("to@example.com", "Subject", "Body");

        verify(mailSender).send(mockMimeMessage);
    }

    @Test
    public void testSendEmailShouldThrowExceptionIfEmailSendingFailed() throws MessagingException {
        when(mimeMessageFactory.createMimeMessage(anyString(), anyString(), anyString())).thenThrow(MessagingException.class);

        assertThrows(IllegalStateException.class,
                () -> emailSenderService.sendEmail("to@example.com", "Subject", "Body"));
    }

}
