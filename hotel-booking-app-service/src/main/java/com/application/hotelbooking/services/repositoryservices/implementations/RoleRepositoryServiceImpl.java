package com.application.hotelbooking.services.repositoryservices.implementations;

import com.application.hotelbooking.domain.RoleModel;
import com.application.hotelbooking.repositories.RoleRepository;
import com.application.hotelbooking.services.repositoryservices.RoleRepositoryService;
import com.application.hotelbooking.transformers.RoleTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class RoleRepositoryServiceImpl implements RoleRepositoryService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleTransformer roleTransformer;

    public RoleModel saveRole(RoleModel roleModel){
        return roleTransformer.transformToRoleModel(roleRepository.save(roleTransformer.transformToRole(roleModel)));
    }
    public Optional<RoleModel> getRoleByName(String roleName){
        return roleTransformer.transformToOptionalRoleModel(roleRepository.findRoleByName(roleName));
    }

    public Collection<RoleModel> getRoles(List<String> roleNames){
        return roleTransformer.transformToRoleModels(roleRepository.findByNameIn(roleNames));
    }
}
