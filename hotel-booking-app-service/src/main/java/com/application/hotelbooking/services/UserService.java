package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.ConfirmationTokenModel;
import com.application.hotelbooking.domain.RoleModel;
import com.application.hotelbooking.domain.User;
import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.exceptions.CredentialMismatchException;
import com.application.hotelbooking.exceptions.EmailAlreadyExistsException;
import com.application.hotelbooking.exceptions.UserAlreadyExistsException;
import com.application.hotelbooking.repositories.UserRepository;
import com.application.hotelbooking.transformers.RoleTransformer;
import com.application.hotelbooking.transformers.UserTransformer;
import jakarta.mail.MessagingException;
import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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
    private EmailSenderService emailSenderService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserTransformer userTransformer;
    @Autowired
    private RoleTransformer roleTransformer;

    public Optional<UserModel> getUserByName(String username){
        return userTransformer.transformToOptionalUserModel(userRepository.findByUsername(username));
    }

    public boolean userExists(String username){
        return getUserByName(username).isPresent();
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
        if (getUserByName(username).isEmpty()){
            throw new IllegalStateException("User does not exist");
        }

        UserModel userModel = getUserByName(username).get();
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

    @Transactional
    public String createUser(String username, String password, String email, List<String> rolesAsStrings) {
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

        if (!isAdmin) {
            LOGGER.info("creating ConfirmationTokenModel");
            String token = UUID.randomUUID().toString();
            ConfirmationTokenModel confirmationTokenModel = ConfirmationTokenModel.builder()
                    .token(token)
                    .user(getUserByName(username).get())
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusMinutes(30))
                    .build();
            LOGGER.info("saving ConfirmationTokenModel");

            confirmationTokenService.saveConfirmationToken(confirmationTokenModel);

            Locale locale = LocaleContextHolder.getLocale();
            String link = "http://localhost:8080/hotelbooking/confirm-token?confirmationToken=" + token;
            String content = messageSource.getMessage("email.confirmation.link.body", null, locale)
                    + "<a href=\""
                    + link
                    + "\">"
                    + messageSource.getMessage("email.confirmation.link.confirm", null, locale)
                    +"</a>";

            emailSenderService.sendEmail(email,
                    messageSource.getMessage("email.confirmation.link.subject", null, locale),
                    content);
        }

        return "Success";
    }

    @Transactional
    public void confirmToken(String token){
        ConfirmationTokenModel confirmationTokenModel = confirmationTokenService
                .findToken(token)
                .orElseThrow(() -> new IllegalStateException("Confirmation token not found."));

        if (confirmationTokenModel.getConfirmedAt() != null){
            throw new IllegalStateException("Email already confirmed.");
        }

        if (confirmationTokenModel.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new IllegalStateException("Token already expired");
        }

        confirmationTokenModel.setConfirmedAt(LocalDateTime.now());
        confirmationTokenService.saveConfirmationToken(confirmationTokenModel);
        enableUser(confirmationTokenModel.getUser().getUsername());
    }

    private void enableUser(String username) {
        UserModel userModel = getUserByName(username).get();
        userModel.setEnabled(true);
        save(userModel);
    }
}
