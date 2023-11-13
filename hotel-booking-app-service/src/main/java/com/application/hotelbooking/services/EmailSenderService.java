package com.application.hotelbooking.services;

public interface EmailSenderService {
    void sendEmail(String toEmail, String subject, String body);
}
