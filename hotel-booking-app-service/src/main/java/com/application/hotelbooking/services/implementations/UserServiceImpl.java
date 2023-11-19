package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.exceptions.*;

import com.application.hotelbooking.services.RoleService;
import com.application.hotelbooking.services.UserService;
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

    public static final long DEFAULT_STARTING_VERSION = 1l;
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "adminadmin";
    public static final String ADMIN_EMAIL = "hotelbookingservice01@gmail.com";

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private UserEmailConfirmationServiceImpl userEmailConfirmationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    public boolean userExists(String username){
        return userRepositoryService.getUserByName(username).isPresent();
    }


    public void createAdminUserIfNotFound(){
        if (userRepositoryService.getUserByEmail(ADMIN_EMAIL).isEmpty()){
            LOGGER.info("Creating admin user");
            createUser(ADMIN_USERNAME, ADMIN_PASSWORD, ADMIN_EMAIL, List.of("ADMIN"));
            LOGGER.info("Created admin user");
        } else {
            LOGGER.info("Admin user already exists");
        }
    }

    public void changePassword(String username, String newPassword, String oldPassword) throws OptimisticLockException{
        if (userRepositoryService.getUserByName(username).isEmpty()){
            throw new IllegalStateException("User does not exist");
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

    public String createUser(String username, String password, String email, List<String> rolesAsStrings) {
        if (userExists(username)){
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
                .roles(roleService.getRoles(rolesAsStrings))
                .build();
        userRepositoryService.save(userModel);

        if (!isAdmin) {
            userEmailConfirmationService.sendConfirmationToken(username, email);
        }

        return "Success";
    }
}
