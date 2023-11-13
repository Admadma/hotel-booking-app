package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.domain.UserModel;

import java.util.Optional;

public interface UserRepositoryService {
    Optional<UserModel> getUserByName(String username);
    Optional<UserModel> getUserByEmail(String email);
    UserModel save(UserModel userModel);
}
