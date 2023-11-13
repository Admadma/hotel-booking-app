package com.application.hotelbooking.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSenderService.class);
    public static final String APPLICATION_EMAIL = "hotelbookingservice01@gmail.com";

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body){
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(APPLICATION_EMAIL);
            helper.setTo(toEmail);
            helper.setText(body, true);
            helper.setSubject(subject);

            mailSender.send(mimeMessage);
        } catch (MessagingException me){
            LOGGER.info("Failed to send email.");
            throw new IllegalStateException("Failed to send email.");
        }

        LOGGER.info("Email sent!");
    }
}
