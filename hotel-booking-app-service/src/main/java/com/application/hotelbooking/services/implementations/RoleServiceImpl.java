package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.domain.RoleModel;
import com.application.hotelbooking.repositories.RoleRepository;
import com.application.hotelbooking.services.RoleService;
import com.application.hotelbooking.services.repositoryservices.RoleRepositoryService;
import com.application.hotelbooking.transformers.RoleTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepositoryService roleRepositoryService;

    public RoleModel createRoleIfNotFound(String roleName){
        Optional<RoleModel> existingRole = roleRepositoryService.getRoleByName(roleName);
        if (existingRole.isEmpty()){
            RoleModel roleToBeCreated = new RoleModel();
            roleToBeCreated.setName(roleName);
            return roleRepositoryService.saveRole(roleToBeCreated);
        }
        return existingRole.get();
    }
}
