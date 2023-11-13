package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.RoleModel;
import com.application.hotelbooking.repositories.RoleRepository;
import com.application.hotelbooking.transformers.RoleTransformer;
import org.springframework.transaction.annotation.Transactional;
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

    public RoleModel createRoleIfNotFound(String roleName){
        if (roleRepository.findRoleByName(roleName).isEmpty()){
            RoleModel roleModel = new RoleModel();
            roleModel.setName(roleName);
            roleRepository.save(roleTransformer.transformToRole(roleModel));
        }
        return roleTransformer.transformToRoleModel(roleRepository.findRoleByName(roleName).get());
    }

    public Collection<RoleModel> getRoles(List<String> roleNames){
        return roleTransformer.transformToRoleModels(
                roleRepository.findAll().stream()
                        .filter(role -> roleNames.contains(role.getName()))
                        .collect(Collectors.toList())
        );
    }
}
