package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.ConfirmationTokenModel;

import java.util.Optional;

public interface ConfirmationTokenService {
    void saveConfirmationToken(ConfirmationTokenModel confirmationTokenModel);
    Optional<ConfirmationTokenModel> findToken(String token);
}
