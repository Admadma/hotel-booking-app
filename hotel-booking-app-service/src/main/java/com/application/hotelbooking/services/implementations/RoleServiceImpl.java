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
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleTransformer roleTransformer;

    @Autowired
    private RoleRepositoryService roleRepositoryService;

    public RoleModel createRoleIfNotFound(String roleName){
        if (roleRepositoryService.getRoleByName(roleName).isEmpty()){
            RoleModel roleModel = new RoleModel();
            roleModel.setName(roleName);
            roleRepository.save(roleTransformer.transformToRole(roleModel));
        }
        return roleRepositoryService.getRoleByName(roleName).get();
    }

    public Collection<RoleModel> getRoles(List<String> roleNames){
        return roleTransformer.transformToRoleModels(roleRepository.findByNameIn(roleNames));
    }


}
