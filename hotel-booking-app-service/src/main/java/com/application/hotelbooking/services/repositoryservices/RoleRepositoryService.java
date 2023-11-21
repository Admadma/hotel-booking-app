package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.domain.RoleModel;

import java.util.Optional;

public interface RoleRepositoryService {

    Optional<RoleModel> getRoleByName(String roleName);
}
