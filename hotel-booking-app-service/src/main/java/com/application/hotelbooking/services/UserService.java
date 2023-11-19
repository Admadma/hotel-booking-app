package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.UserModel;
import jakarta.persistence.OptimisticLockException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    @Transactional
    void createAdminUserIfNotFound();
    @Transactional
    UserModel createUser(String username, String password, String email, List<String> rolesAsStrings);
    void changePassword(String username, String newPassword, String oldPassword) throws OptimisticLockException;
}
