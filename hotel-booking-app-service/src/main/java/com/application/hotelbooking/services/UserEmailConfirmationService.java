package com.application.hotelbooking.services;

import org.springframework.transaction.annotation.Transactional;

public interface UserEmailConfirmationService {
    @Transactional
    void confirmToken(String token);
    @Transactional
    void resendConfirmationToken(String email);
}
