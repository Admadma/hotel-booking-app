package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.domain.RoleModel;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RoleRepositoryService {

    RoleModel saveRole(RoleModel roleModel);
    Optional<RoleModel> getRoleByName(String roleName);
    Collection<RoleModel> getRoles(List<String> roleNames);
}
