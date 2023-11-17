package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.domain.ConfirmationTokenModel;
import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.exceptions.*;

import com.application.hotelbooking.services.ConfirmationTokenService;
import com.application.hotelbooking.services.EmailSenderService;
import com.application.hotelbooking.services.RoleService;
import com.application.hotelbooking.services.UserService;
import com.application.hotelbooking.services.repositoryservices.UserRepositoryService;
import com.application.hotelbooking.transformers.RoleTransformer;
import com.application.hotelbooking.transformers.UserTransformer;
import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public boolean userExists(String username){
        return userRepositoryService.getUserByName(username).isPresent();
    }

    private boolean emailExists(String email) {
        return userRepositoryService.getUserByEmail(email).isPresent();
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
        if (emailExists(email)){
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
            sendConfirmationToken(username, email);
        }

        return "Success";
    }

    public void resendConfirmationToken(String email){
        if (!emailExists(email)){
            throw new InvalidUserException("There is no user with that email");
        }
        if (userRepositoryService.getUserByEmail(email).get().getEnabled()){
            throw new EmailAlreadyConfirmedException("That email is already confirmed");
        }

        sendConfirmationToken(userRepositoryService.getUserByEmail(email).get().getUsername(), email);
    }

    private void sendConfirmationToken(String username, String email) {
        LOGGER.info("creating ConfirmationTokenModel");
        String token = UUID.randomUUID().toString();
        ConfirmationTokenModel confirmationTokenModel = ConfirmationTokenModel.builder()
                .token(token)
                .user(userRepositoryService.getUserByName(username).get())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();
        LOGGER.info("saving ConfirmationTokenModel");

        confirmationTokenService.saveConfirmationToken(confirmationTokenModel);

        Locale locale = LocaleContextHolder.getLocale();
        String link = "http://localhost:8080/hotelbooking/register/confirmemail/confirm-token?confirmationToken=" + token;
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

    public void confirmToken(String token){
        ConfirmationTokenModel confirmationTokenModel = confirmationTokenService
                .findToken(token)
                .orElseThrow(() -> new InvalidTokenException("Confirmation token not found."));

        if (confirmationTokenModel.getConfirmedAt() != null){
            throw new EmailAlreadyConfirmedException("Email already confirmed.");
        }

        if (confirmationTokenModel.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new ExpiredTokenException("Token already expired");
        }

        confirmationTokenModel.setConfirmedAt(LocalDateTime.now());
        confirmationTokenService.saveConfirmationToken(confirmationTokenModel);
        enableUser(confirmationTokenModel.getUser().getUsername());
    }

    private void enableUser(String username) {
        UserModel userModel = userRepositoryService.getUserByName(username).get();
        userModel.setEnabled(true);
        userRepositoryService.save(userModel);
    }
}
