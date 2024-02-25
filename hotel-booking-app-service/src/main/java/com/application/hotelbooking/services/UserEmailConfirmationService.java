package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.UserModel;
import org.springframework.transaction.annotation.Transactional;

public interface UserEmailConfirmationService {
    @Transactional
    void confirmToken(String token);
    @Transactional
    void sendConfirmationToken(UserModel user);
}
