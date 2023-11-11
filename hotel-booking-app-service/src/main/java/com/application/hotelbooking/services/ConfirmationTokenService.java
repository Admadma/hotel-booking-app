package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.ConfirmationTokenModel;
import com.application.hotelbooking.repositories.ConfirmationTokenRepository;
import com.application.hotelbooking.transformers.ConfirmationTokenTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private ConfirmationTokenTransformer confirmationTokenTransformer;

    public void saveConfirmationToken(ConfirmationTokenModel confirmationTokenModel){
        confirmationTokenRepository.save(confirmationTokenTransformer.transformToConfirmationToken(confirmationTokenModel));
    }
}
