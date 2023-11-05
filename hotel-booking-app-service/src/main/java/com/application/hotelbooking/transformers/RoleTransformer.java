package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.Role;
import com.application.hotelbooking.domain.RoleModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class RoleTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public RoleModel transformToRoleModel(Role role){
        return modelMapper.map(role, RoleModel.class);
    }

    public Role transformToRole(RoleModel roleModel){
        return modelMapper.map(roleModel, Role.class);
    }

    public Collection<RoleModel> transformToRoleModels(Collection<Role> roles){
        return roles.stream()
                .map(role -> modelMapper.map(role, RoleModel.class))
                .collect(Collectors.toList());
    }
}
