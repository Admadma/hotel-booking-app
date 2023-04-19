package com.application.hotelbooking.services;


import com.application.hotelbooking.domain.Role;
import com.application.hotelbooking.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public Role createRoleIfNotFound(String roleName){
        Role role = roleRepository.findRoleByName(roleName);
        if (role == null){
            role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
        return roleRepository.findRoleByName(roleName);
    }
}
