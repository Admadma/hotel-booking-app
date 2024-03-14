package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.domain.ConfirmationTokenModel;
import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.services.UserService;
import com.application.hotelbooking.services.repositoryservices.ConfirmationTokenRepositoryService;
import com.application.hotelbooking.services.EmailSenderService;
import com.application.hotelbooking.services.UserEmailConfirmationSenderService;
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
public class UserEmailConfirmationSenderServiceImpl implements UserEmailConfirmationSenderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEmailConfirmationSenderServiceImpl.class);
    public static final String BASE_LINK = "http://localhost:8080/hotelbooking/register/confirmemail/confirm-token?confirmationToken=";
    @Autowired
    private ConfirmationTokenRepositoryService confirmationTokenRepositoryService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private MessageSource messageSource;

    public void sendConfirmationToken(UserModel user) {
        LOGGER.info("creating ConfirmationTokenModel");
        String token = UUID.randomUUID().toString();
        ConfirmationTokenModel confirmationTokenModel = ConfirmationTokenModel.builder()
                .token(token)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();

        LOGGER.info("saving ConfirmationTokenModel");
        confirmationTokenRepositoryService.saveConfirmationToken(confirmationTokenModel);

        Locale locale = LocaleContextHolder.getLocale();
        String link = BASE_LINK + token;
        String body = getBody(locale, link);
        emailSenderService.sendEmail(user.getEmail(),
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
}