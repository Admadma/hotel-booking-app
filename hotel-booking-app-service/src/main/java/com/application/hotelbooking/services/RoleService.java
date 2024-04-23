package com.application.hotelbooking.services;

import com.application.hotelbooking.models.RoleModel;
import org.springframework.transaction.annotation.Transactional;

public interface RoleService {
    @Transactional
    RoleModel createRoleIfNotFound(String roleName);
}
