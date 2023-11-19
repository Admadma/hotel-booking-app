package com.application.hotelbooking.services;

import jakarta.persistence.OptimisticLockException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    @Transactional
    void createAdminUserIfNotFound();
    @Transactional
    String createUser(String username, String password, String email, List<String> rolesAsStrings);
    void changePassword(String username, String newPassword, String oldPassword) throws OptimisticLockException;
}
