package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.domain.ConfirmationTokenModel;
import com.application.hotelbooking.exceptions.EmailAlreadyConfirmedException;
import com.application.hotelbooking.exceptions.ExpiredTokenException;
import com.application.hotelbooking.exceptions.InvalidTokenException;
import com.application.hotelbooking.services.UserEmailTokenConfirmationService;
import com.application.hotelbooking.services.UserService;
import com.application.hotelbooking.services.repositoryservices.ConfirmationTokenRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserEmailTokenConfirmationServiceImpl implements UserEmailTokenConfirmationService{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEmailTokenConfirmationService.class);
    @Autowired
    private ConfirmationTokenRepositoryService confirmationTokenRepositoryService;
    @Autowired
    private UserService userService;

    public void confirmToken(String token){
        ConfirmationTokenModel confirmationTokenModel = confirmationTokenRepositoryService
                .getByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Confirmation token not found."));

        if (confirmationTokenModel.getConfirmedAt() != null){
            throw new EmailAlreadyConfirmedException("Email already confirmed.");
        }

        if (confirmationTokenModel.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new ExpiredTokenException("Token already expired");
        }

        confirmationTokenModel.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepositoryService.saveConfirmationToken(confirmationTokenModel);
        userService.enableUser(confirmationTokenModel.getUser().getEmail());
    }
}
