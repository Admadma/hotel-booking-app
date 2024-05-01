package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.factories.MimeMessageFactory;
import com.application.hotelbooking.services.EmailSenderService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSenderService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MimeMessageFactory mimeMessageFactory;

    public void sendEmail(String toEmail, String subject, String body){
        try {
            mailSender.send(mimeMessageFactory.createMimeMessage(toEmail, subject, body));
        } catch (MessagingException me){
            LOGGER.info("Failed to send email.");
            throw new IllegalStateException("Failed to send email.");
        }

        LOGGER.info("Email sent!");
    }
}
