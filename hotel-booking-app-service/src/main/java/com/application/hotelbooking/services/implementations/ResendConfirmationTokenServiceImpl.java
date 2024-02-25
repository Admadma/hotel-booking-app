package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.exceptions.EmailAlreadyConfirmedException;
import com.application.hotelbooking.exceptions.InvalidUserException;
import com.application.hotelbooking.services.ResendConfirmationTokenService;
import com.application.hotelbooking.services.UserEmailConfirmationService;
import com.application.hotelbooking.services.repositoryservices.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResendConfirmationTokenServiceImpl implements ResendConfirmationTokenService {

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private UserEmailConfirmationService userEmailConfirmationService;

    public void resendConfirmationToken(String email){
        if (!userRepositoryService.emailExists(email)){
            throw new InvalidUserException("There is no user with that email");
        }
        if (userRepositoryService.getUserByEmail(email).get().getEnabled()){
            throw new EmailAlreadyConfirmedException("That email is already confirmed");
        }

        userEmailConfirmationService.sendConfirmationToken(userRepositoryService.getUserByEmail(email).get());
    }
}
