package com.application.hotelbooking.security;

import com.application.hotelbooking.services.RoleService;
import com.application.hotelbooking.services.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

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

        userService.createAdminUserIfNotFound();

        alreadySetup = true;
    }
}
