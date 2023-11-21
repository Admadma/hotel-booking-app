package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.RoleModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface RoleService {
    @Transactional
    RoleModel createRoleIfNotFound(String roleName);
}
