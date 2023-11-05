package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.Role;
import com.application.hotelbooking.dto.RoleDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class RoleTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public RoleDTO transformToRoleDTO(Role role){
        return modelMapper.map(role, RoleDTO.class);
    }

    public Role transformToRole(RoleDTO roleDTO){
        return modelMapper.map(roleDTO, Role.class);
    }

    public Collection<RoleDTO> transformToRoleDTOs(Collection<Role> roles){
        return roles.stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .collect(Collectors.toList());
    }
}
