package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.domain.RoleModel;
import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.exceptions.*;

import com.application.hotelbooking.services.UserService;
import com.application.hotelbooking.services.repositoryservices.RoleRepositoryService;
import com.application.hotelbooking.services.repositoryservices.UserRepositoryService;
import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private UserEmailConfirmationSenderServiceImpl userEmailConfirmationSenderService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepositoryService roleRepositoryService;

    public void changePassword(String username, String newPassword, String oldPassword) throws OptimisticLockException{
        if (userRepositoryService.getUserByName(username).isEmpty()){
            throw new InvalidUserException("User does not exist");
        }

        UserModel userModel = userRepositoryService.getUserByName(username).get();
        if (!oldPasswordMatches(userModel, oldPassword)){
            throw new CredentialMismatchException("The provided old password does not match.");
        }

        userModel.setPassword(passwordEncoder.encode(newPassword));
        userRepositoryService.save(userModel);
    }

    private boolean oldPasswordMatches(UserModel userModel, String oldPassword){
        return passwordEncoder.matches(oldPassword, userModel.getPassword());
    }

    public UserModel createUser(String username, String password, String email, List<String> rolesAsStrings) throws UserAlreadyExistsException, EmailAlreadyExistsException{
        if (userRepositoryService.userExists(username)){
            throw new UserAlreadyExistsException("That username is already taken");
        }
        if (userRepositoryService.emailExists(email)){
            throw new EmailAlreadyExistsException("That email is already taken");
        }

        boolean isAdmin = rolesAsStrings.contains("ADMIN");
        UserModel userModel = UserModel.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .enabled(isAdmin) //We want to skip the email validation process and enable admin users by default
                .roles(roleRepositoryService.getRoles(rolesAsStrings))
                .build();
        UserModel savedUser = userRepositoryService.save(userModel);

        if (!isAdmin) {
            userEmailConfirmationSenderService.sendConfirmationToken(savedUser);
        }

        return savedUser;
    }

    public UserModel enableUser(String email) {
        UserModel userModel = userRepositoryService.getUserByEmail(email).get();
        userModel.setEnabled(true);
        return userRepositoryService.save(userModel);
    }

    public boolean userHasRole(String username, String role){
        for (RoleModel roleModel : userRepositoryService.getUserByName(username).get().getRoles()) {
            if (roleModel.getName().equals(role)){
                return true;
            }
        }

        return false;
    }
}
