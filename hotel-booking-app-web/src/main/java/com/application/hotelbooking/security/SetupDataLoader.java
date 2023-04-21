package com.application.hotelbooking.security;

import com.application.hotelbooking.domain.Role;
import com.application.hotelbooking.domain.RoleView;
import com.application.hotelbooking.services.RoleService;
import com.application.hotelbooking.services.UserService;
import com.application.hotelbooking.transformers.RoleViewTransformer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleViewTransformer roleViewTransformer;


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup)
            return;
        RoleView adminRole = roleViewTransformer.transformToRoleView(roleService.createRoleIfNotFound("ADMIN"));
        RoleView userRole = roleViewTransformer.transformToRoleView(roleService.createRoleIfNotFound("USER"));

        userService.createAdminUserIfNotFound(
                "admin",
                "adminadmin",
                roleViewTransformer.transformToRoleModels(List.of(adminRole)));


        alreadySetup = true;
    }
}
