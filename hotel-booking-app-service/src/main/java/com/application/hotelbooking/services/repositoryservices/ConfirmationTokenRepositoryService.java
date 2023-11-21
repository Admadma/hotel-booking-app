package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.domain.ConfirmationTokenModel;

import java.util.Optional;

public interface ConfirmationTokenRepositoryService {
    void saveConfirmationToken(ConfirmationTokenModel confirmationTokenModel);
    Optional<ConfirmationTokenModel> findToken(String token);
}
