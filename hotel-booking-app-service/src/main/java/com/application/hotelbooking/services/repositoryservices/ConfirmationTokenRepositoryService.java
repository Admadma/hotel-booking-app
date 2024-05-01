package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.models.ConfirmationTokenModel;

import java.util.Optional;

public interface ConfirmationTokenRepositoryService {
    ConfirmationTokenModel saveConfirmationToken(ConfirmationTokenModel confirmationTokenModel);
    Optional<ConfirmationTokenModel> getByToken(String token);
}
