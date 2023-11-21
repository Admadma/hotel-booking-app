package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.domain.ConfirmationTokenModel;
import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.exceptions.EmailAlreadyConfirmedException;
import com.application.hotelbooking.exceptions.ExpiredTokenException;
import com.application.hotelbooking.exceptions.InvalidTokenException;
import com.application.hotelbooking.exceptions.InvalidUserException;
import com.application.hotelbooking.services.repositoryservices.ConfirmationTokenRepositoryService;
import com.application.hotelbooking.services.EmailSenderService;
import com.application.hotelbooking.services.UserEmailConfirmationService;
import com.application.hotelbooking.services.repositoryservices.UserRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
public class UserEmailConfirmationServiceImpl implements UserEmailConfirmationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEmailConfirmationServiceImpl.class);
    public static final String BASE_LINK = "http://localhost:8080/hotelbooking/register/confirmemail/confirm-token?confirmationToken=";
    @Autowired
    private ConfirmationTokenRepositoryService confirmationTokenRepositoryService;
    @Autowired
    private UserRepositoryService userRepositoryService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private MessageSource messageSource;

    public void resendConfirmationToken(String email){
        if (!userRepositoryService.emailExists(email)){
            throw new InvalidUserException("There is no user with that email");
        }
        if (userRepositoryService.getUserByEmail(email).get().getEnabled()){
            throw new EmailAlreadyConfirmedException("That email is already confirmed");
        }

        sendConfirmationToken(userRepositoryService.getUserByEmail(email).get().getUsername(), email);
    }

    public void sendConfirmationToken(String username, String email) {
        LOGGER.info("creating ConfirmationTokenModel");
        String token = UUID.randomUUID().toString();
        ConfirmationTokenModel confirmationTokenModel = ConfirmationTokenModel.builder()
                .token(token)
                .user(userRepositoryService.getUserByName(username).get())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();

        LOGGER.info("saving ConfirmationTokenModel");
        confirmationTokenRepositoryService.saveConfirmationToken(confirmationTokenModel);

        Locale locale = LocaleContextHolder.getLocale();
        String link = BASE_LINK + token;
        String body = getBody(locale, link);
        emailSenderService.sendEmail(email,
                messageSource.getMessage("email.confirmation.link.subject", null, locale),
                body);
    }

    private String getBody(Locale locale, String link) {
        return messageSource.getMessage("email.confirmation.link.body", null, locale)
                + "<a href=\""
                + link
                + "\">"
                + messageSource.getMessage("email.confirmation.link.confirm", null, locale)
                + "</a>";
    }

    public void confirmToken(String token){
        ConfirmationTokenModel confirmationTokenModel = confirmationTokenRepositoryService
                .findToken(token)
                .orElseThrow(() -> new InvalidTokenException("Confirmation token not found."));

        if (confirmationTokenModel.getConfirmedAt() != null){
            throw new EmailAlreadyConfirmedException("Email already confirmed.");
        }

        if (confirmationTokenModel.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new ExpiredTokenException("Token already expired");
        }

        confirmationTokenModel.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepositoryService.saveConfirmationToken(confirmationTokenModel);
        enableUser(confirmationTokenModel.getUser().getUsername());
    }

    private void enableUser(String username) {
        UserModel userModel = userRepositoryService.getUserByName(username).get();
        userModel.setEnabled(true);
        userRepositoryService.save(userModel);
    }
}
