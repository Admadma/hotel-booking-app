package com.application.hotelbooking.security;

import com.application.hotelbooking.exceptions.UserAlreadyExistsException;
import com.application.hotelbooking.services.RoleService;
import com.application.hotelbooking.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetupDataLoader.class);
    private final String ADMIN_USERNAME;
    private final String ADMIN_PASSWORD;
    private final String APPLICATION_EMAIL;

    @Autowired
    public SetupDataLoader(@Value("${ADMIN_USERNAME}") String adminUsername,
                           @Value("${ADMIN_PASSWORD}") String adminPassword,
                           @Value("${APPLICATION_EMAIL}") String applicationEmail) {
        this.ADMIN_USERNAME = adminUsername;
        this.ADMIN_PASSWORD = adminPassword;
        this.APPLICATION_EMAIL = applicationEmail;
    }

    boolean alreadySetup = false;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup)
            return;
        roleService.createRoleIfNotFound("ADMIN");
        roleService.createRoleIfNotFound("USER");

        try {
            userService.createUser(ADMIN_USERNAME, ADMIN_PASSWORD, APPLICATION_EMAIL, List.of("ADMIN"));
        } catch (UserAlreadyExistsException uae){
            LOGGER.info("Admin user already exists");
        }

        alreadySetup = true;
    }
}
