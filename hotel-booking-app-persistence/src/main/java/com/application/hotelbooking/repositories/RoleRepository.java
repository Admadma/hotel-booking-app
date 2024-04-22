package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findRoleByName(String name);
    Collection<Role> findByNameIn(List<String> roleNames);
}
