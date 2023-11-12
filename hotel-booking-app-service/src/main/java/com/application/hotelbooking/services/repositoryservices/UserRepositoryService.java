package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.repositories.UserRepository;
import com.application.hotelbooking.transformers.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRepositoryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTransformer userTransformer;

    public Optional<UserModel> getUserByName(String username){
        return userTransformer.transformToOptionalUserModel(userRepository.findByUsername(username));
    }

    public Optional<UserModel> getUserByEmail(String email){
        return userTransformer.transformToOptionalUserModel(userRepository.findByEmail(email));
    }

    public UserModel save(UserModel userModel){
        return userTransformer.transformToUserModel(userRepository.save(userTransformer.transformToUser(userModel)));
    }
}
