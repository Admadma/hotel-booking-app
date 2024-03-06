package com.application.hotelbooking.services.repositoryservices.implementations;

import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.repositories.UserRepository;
import com.application.hotelbooking.services.repositoryservices.UserRepositoryService;
import com.application.hotelbooking.transformers.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRepositoryServiceImpl implements UserRepositoryService {

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

    public boolean userExists(String username){
        return userRepository.existsByUsername(username);
    }
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
