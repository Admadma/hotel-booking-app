package com.application.hotelbooking.security;

import com.application.hotelbooking.exceptions.UserAlreadyExistsException;
import com.application.hotelbooking.services.RoleService;
import com.application.hotelbooking.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetupDataLoader.class);

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
        //TODO: Load admin credentials from properties file or environment variable

        try {
            userService.createUser("admin", "adminadmin", "hotelbookingservice01@gmail.com", List.of("ADMIN"));
        } catch (UserAlreadyExistsException uae){
            LOGGER.info("Admin user already exists");
        }

        alreadySetup = true;
    }
}
