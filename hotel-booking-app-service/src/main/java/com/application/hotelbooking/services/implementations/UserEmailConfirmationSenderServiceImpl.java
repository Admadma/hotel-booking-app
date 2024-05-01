package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.models.ConfirmationTokenModel;
import com.application.hotelbooking.models.UserModel;
import com.application.hotelbooking.services.repositoryservices.ConfirmationTokenRepositoryService;
import com.application.hotelbooking.services.EmailSenderService;
import com.application.hotelbooking.services.UserEmailConfirmationSenderService;
import com.application.hotelbooking.wrappers.UUIDWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class UserEmailConfirmationSenderServiceImpl implements UserEmailConfirmationSenderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEmailConfirmationSenderServiceImpl.class);
    private final String TOKEN_CONFIRMATION_LINK;

    public UserEmailConfirmationSenderServiceImpl(@Value("${APPLICATION_BASE_URL}") String applicationBaseURL) {
        this.TOKEN_CONFIRMATION_LINK = applicationBaseURL + "hotelbooking/register/confirm-email/confirm-token?confirmationToken=";
    }

    @Autowired
    private ConfirmationTokenRepositoryService confirmationTokenRepositoryService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UUIDWrapper uuidWrapper;

    public void sendConfirmationToken(UserModel user) {
        LOGGER.info("Creating ConfirmationTokenModel");
        String token = uuidWrapper.getRandomUUID().toString();
        ConfirmationTokenModel confirmationTokenModel = ConfirmationTokenModel.builder()
                .token(token)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();

        LOGGER.info("Saving ConfirmationTokenModel");
        confirmationTokenRepositoryService.saveConfirmationToken(confirmationTokenModel);

        Locale locale = LocaleContextHolder.getLocale();
        String link = TOKEN_CONFIRMATION_LINK + token;
        String body = getBody(locale, link);
        LOGGER.info("Sending email");
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
