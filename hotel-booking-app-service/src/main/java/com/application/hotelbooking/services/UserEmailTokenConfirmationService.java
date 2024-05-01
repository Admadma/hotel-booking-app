package com.application.hotelbooking.services;

import org.springframework.transaction.annotation.Transactional;

public interface UserEmailTokenConfirmationService {
    @Transactional
    void confirmToken(String token);
}
