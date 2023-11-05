package com.application.hotelbooking.services;

import com.application.hotelbooking.dto.RoleDTO;
import com.application.hotelbooking.repositories.RoleRepository;
import com.application.hotelbooking.transformers.RoleTransformer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleTransformer roleTransformer;

    @Transactional
    public RoleDTO createRoleIfNotFound(String roleName){
        if (roleRepository.findRoleByName(roleName) == null){
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setName(roleName);
            roleRepository.save(roleTransformer.transformToRole(roleDTO));
        }
        return roleTransformer.transformToRoleDTO(roleRepository.findRoleByName(roleName));
    }

    private boolean roleExists(String roleName) {
        return roleRepository.findRoleByName(roleName) == null;
    }

    public Collection<RoleDTO> getRoles(List<String> roleNames){
        return roleTransformer.transformToRoleDTOs(
                roleRepository.findAll().stream()
                        .filter(role -> roleNames.contains(role.getName()))
                        .collect(Collectors.toList())
        );
    }
}
