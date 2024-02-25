package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.exceptions.EmailAlreadyExistsException;
import com.application.hotelbooking.exceptions.UserAlreadyExistsException;
import jakarta.persistence.OptimisticLockException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    UserModel createUser(String username, String password, String email, List<String> rolesAsStrings) throws UserAlreadyExistsException, EmailAlreadyExistsException;
    void changePassword(String username, String newPassword, String oldPassword) throws OptimisticLockException;
    UserModel enableUser(String email);
}
