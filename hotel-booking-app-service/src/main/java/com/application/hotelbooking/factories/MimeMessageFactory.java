package com.application.hotelbooking.factories;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MimeMessageFactory {

    private final String APPLICATION_EMAIL;

    @Autowired
    public MimeMessageFactory(Dotenv dotenv) {
        this.APPLICATION_EMAIL = dotenv.get("APPLICATION_EMAIL");
    }

    @Autowired
    private JavaMailSender mailSender;

    public MimeMessage createMimeMessage(String toEmail, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(APPLICATION_EMAIL);
        helper.setTo(toEmail);
        helper.setText(body, true);
        helper.setSubject(subject);
        return helper.getMimeMessage();
    }
}
