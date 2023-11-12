package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.ConfirmationTokenModel;
import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.repositories.ConfirmationTokenRepository;
import com.application.hotelbooking.services.repositoryservices.UserRepositoryService;
import com.application.hotelbooking.transformers.ConfirmationTokenTransformer;
import com.application.hotelbooking.transformers.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private ConfirmationTokenTransformer confirmationTokenTransformer;

    @Autowired
    private UserTransformer userTransformer;

    public void saveConfirmationToken(ConfirmationTokenModel confirmationTokenModel){
        confirmationTokenRepository.save(confirmationTokenTransformer.transformToConfirmationToken(confirmationTokenModel));
    }

    public Optional<ConfirmationTokenModel> findToken(String token){
        return confirmationTokenTransformer.transformToOptionalConfirmationTokenModel(confirmationTokenRepository.findByToken(token));
    }

    public List<ConfirmationTokenModel> getAllTokensOfUser(String email){
        return confirmationTokenTransformer.transformToConfirmationTokenModels(
                confirmationTokenRepository.findByUser(
                        userTransformer.transformToUser(
                                userRepositoryService.getUserByEmail(email).get())));
    }
}
