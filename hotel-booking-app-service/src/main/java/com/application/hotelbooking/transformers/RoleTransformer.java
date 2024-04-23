package com.application.hotelbooking.transformers;

import com.application.hotelbooking.entities.Role;
import com.application.hotelbooking.models.RoleModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public Role transformToRole(RoleModel roleModel){
        return modelMapper.map(roleModel, Role.class);
    }

    public RoleModel transformToRoleModel(Role role){
        return modelMapper.map(role, RoleModel.class);
    }

    public Optional<RoleModel> transformToOptionalRoleModel(Optional<Role> role){
        if (role.isPresent()){
            return Optional.of(modelMapper.map(role, RoleModel.class));
        } else {
            return Optional.empty();
        }
    }

    public Collection<RoleModel> transformToRoleModels(Collection<Role> roles){
        return roles.stream()
                .map(role -> modelMapper.map(role, RoleModel.class))
                .collect(Collectors.toList());
    }
}
