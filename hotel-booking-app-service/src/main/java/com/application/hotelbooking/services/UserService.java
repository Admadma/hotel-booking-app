package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.ConfirmationTokenModel;
import com.application.hotelbooking.domain.RoleModel;
import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.exceptions.CredentialMismatchException;
import com.application.hotelbooking.exceptions.EmailAlreadyExistsException;
import com.application.hotelbooking.exceptions.UserAlreadyExistsException;
import com.application.hotelbooking.repositories.UserRepository;
import com.application.hotelbooking.transformers.RoleTransformer;
import com.application.hotelbooking.transformers.UserTransformer;
import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public static final long DEFAULT_STARTING_VERSION = 1l;
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "adminadmin";
    public static final String ADMIN_EMAIL = "hotelbookingservice01@gmail.com";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private UserTransformer userTransformer;
    @Autowired
    private RoleTransformer roleTransformer;

    public List<UserModel> getUsersByName(String username){
        return userTransformer.transformToUserModels(userRepository.findUserByUsername(username)).stream().toList();
    }

    public boolean userExists(String username){
        return getUsersByName(username).size() == 1;
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private UserModel save(UserModel userModel){
        return userTransformer.transformToUserModel(userRepository.save(userTransformer.transformToUser(userModel)));
    }

    @Transactional
    public void createAdminUserIfNotFound(){
        if (userRepository.findByEmail(ADMIN_EMAIL).isEmpty()){
            LOGGER.info("Creating admin user");
            createUser(ADMIN_USERNAME, ADMIN_PASSWORD, ADMIN_EMAIL, List.of("ADMIN"));
            LOGGER.info("Created admin user");
        } else {
            LOGGER.info("Admin user already exists");
        }
    }

    public void changePassword(String username, String newPassword, String oldPassword, Long version) throws OptimisticLockException{
        UserModel userModel = getUsersByName(username).get(0);
        if (!userVersionMatches(version, userModel)){
            throw new OptimisticLockException();
        }
        if (!oldPasswordMatches(userModel, oldPassword)){
            throw new CredentialMismatchException("The provided old password does mot match.");
        }

        userModel.setPassword(passwordEncoder.encode(newPassword));
        userModel.setVersion(++version);
        save(userModel);
    }

    private boolean userVersionMatches(Long version, UserModel userModel) {
        return userModel.getVersion().equals(version);
    }

    private boolean oldPasswordMatches(UserModel userModel, String oldPassword){
        return passwordEncoder.matches(oldPassword, userModel.getPassword());
    }

    //TODO: return the UserModel (if I want to do something with that information)
    @Transactional
    public String createUser(String username, String password, String email, List<String> rolesAsStrings){
        //TODO: validate the email format
        if (userExists(username)){
            throw new UserAlreadyExistsException("That username is already taken");
        }
        if (emailExists(email)){
            throw new EmailAlreadyExistsException("That email is already taken");
        }

        boolean isAdmin = rolesAsStrings.contains("ADMIN");
        UserModel userModel = UserModel.builder()
                .username(username)
                .version(DEFAULT_STARTING_VERSION)
                .password(passwordEncoder.encode(password))
                .email(email)
                .enabled(isAdmin) //We want to skip the email validation process and enable admin users by default
                .roles(roleService.getRoles(rolesAsStrings))
                .build();
        save(userModel);
        LOGGER.info("Saved");

        if (!isAdmin) {
            String token = UUID.randomUUID().toString();
            ConfirmationTokenModel confirmationTokenModel = ConfirmationTokenModel.builder()
                    .token(token)
                    .user(getUsersByName(username).get(0))
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusMinutes(30))
                    .build();

            confirmationTokenService.saveConfirmationToken(confirmationTokenModel);

        }

        return "Success";
    }

    //Simple logic for enabling user. Not used anywhere yet
    @Transactional
    public void confirmToken(String username){
        UserModel userModel = getUsersByName(username).get(0);
        userModel.setEnabled(true);
        save(userModel);
    }
}
