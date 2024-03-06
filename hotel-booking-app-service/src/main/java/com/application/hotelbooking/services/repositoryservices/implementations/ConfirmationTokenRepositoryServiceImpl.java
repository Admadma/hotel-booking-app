package com.application.hotelbooking.services.repositoryservices.implementations;

import com.application.hotelbooking.domain.ConfirmationTokenModel;
import com.application.hotelbooking.repositories.ConfirmationTokenRepository;
import com.application.hotelbooking.services.repositoryservices.ConfirmationTokenRepositoryService;
import com.application.hotelbooking.transformers.ConfirmationTokenTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfirmationTokenRepositoryServiceImpl implements ConfirmationTokenRepositoryService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private ConfirmationTokenTransformer confirmationTokenTransformer;

    public ConfirmationTokenModel saveConfirmationToken(ConfirmationTokenModel confirmationTokenModel){
        return confirmationTokenTransformer.transformToConfirmationTokenModel(confirmationTokenRepository.save(confirmationTokenTransformer.transformToConfirmationToken(confirmationTokenModel)));
    }

    public Optional<ConfirmationTokenModel> getByToken(String token){
        return confirmationTokenTransformer.transformToOptionalConfirmationTokenModel(confirmationTokenRepository.findByToken(token));
    }
}
