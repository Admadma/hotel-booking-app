package com.application.hotelbooking.services;

import jakarta.persistence.OptimisticLockException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    @Transactional
    void createAdminUserIfNotFound();
    @Transactional
    String createUser(String username, String password, String email, List<String> rolesAsStrings);
    @Transactional
    void confirmToken(String token);
    @Transactional
    void resendConfirmationToken(String email);
    void changePassword(String username, String newPassword, String oldPassword) throws OptimisticLockException;
}
