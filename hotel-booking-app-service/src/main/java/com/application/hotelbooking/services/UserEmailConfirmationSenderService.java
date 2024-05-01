package com.application.hotelbooking.services;

import com.application.hotelbooking.models.UserModel;
import org.springframework.transaction.annotation.Transactional;

public interface UserEmailConfirmationSenderService {
    @Transactional
    void sendConfirmationToken(UserModel user);
}
