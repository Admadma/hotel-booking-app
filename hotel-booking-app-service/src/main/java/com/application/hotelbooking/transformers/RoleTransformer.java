package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.Role;
import com.application.hotelbooking.domain.RoleModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public RoleModel transformToModel(Role role){
        return modelMapper.map(role, RoleModel.class);
    }

    public Role transformToRole(RoleModel roleModel){
        return modelMapper.map(roleModel, Role.class);
    }

//    public List<RoleModel> transformToModels(List<Role> roles){
//        return modelMapper.map(role, RoleModel.class);
//    }

    public List<Role> transformToRoles(Collection<RoleModel> roleModels){
        List<Role> roles = roleModels.stream()
                .map(roleModel -> modelMapper.map(roleModel, Role.class))
                .collect(Collectors.toList());

        roles.stream().forEach(role -> System.out.println(role.getName()));
        return roles;
    }
}
