package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testSaveReturnsSavedRole(){
        User testUser1 = User.builder()
                .username("user_1")
                .build();
        User testUser2 = User.builder()
                .username("user_2")
                .build();
        Role role = Role.builder()
                .name("TEST_USER")
                .users(Arrays.asList(testUser1, testUser2))
                .build();

        Role savedRole = roleRepository.save(role);

        Assertions.assertThat(savedRole).isNotNull();
        Assertions.assertThat(savedRole.getId()).isNotNull();
        Assertions.assertThat(savedRole.getName()).isEqualTo(role.getName());
        Assertions.assertThat(savedRole.getUsers()).isEqualTo(role.getUsers());
    }

    @Test
    public void testFindAllReturnsAllRoles(){
        Role role1 = roleRepository.save(Role.builder()
                .name("TEST_USER")
                .build());
        Role role2 = roleRepository.save(Role.builder()
                .name("TEST_USER")
                .build());

        List<Role> resultRoles = roleRepository.findAll();

        Assertions.assertThat(resultRoles).isNotNull();
        Assertions.assertThat(resultRoles).isNotEmpty();
        Assertions.assertThat(resultRoles).isEqualTo(List.of(role1, role2));
    }

    @Test
    public void testFindAllReturnsEmptyListIfNoRolesAdded(){

        List<Role> resultRoles = roleRepository.findAll();

        Assertions.assertThat(resultRoles).isNotNull();
        Assertions.assertThat(resultRoles).isEmpty();
    }

    @Test
    public void testFindRoleByNameReturnsOptionalOfRoleWithProvidedName(){
        Role role = roleRepository.save(Role.builder()
                .name("TEST_USER")
                .build());

        Optional<Role> resultRole = roleRepository.findRoleByName("TEST_USER");

        Assertions.assertThat(resultRole).isNotNull();
        Assertions.assertThat(resultRole).isNotEmpty();
        Assertions.assertThat(resultRole.get().getName()).isEqualTo(role.getName());
    }

    @Test
    public void testFindRoleByNameReturnsEmptyOptionalWithNonexistentNameProvided(){

        Optional<Role> resultRole = roleRepository.findRoleByName("TEST_USER");

        Assertions.assertThat(resultRole).isNotNull();
        Assertions.assertThat(resultRole).isEmpty();
    }

    @Test
    public void testFindByRoleNameInShouldReturnCollectionOfRolesWhereRoleNameInList(){
        Role role1 = roleRepository.save(Role.builder()
                .name("TEST_ROLE_1")
                .build());
        Role role2 = roleRepository.save(Role.builder()
                .name("TEST_ROLE_2")
                .build());
        Role role3 = roleRepository.save(Role.builder()
                .name("TEST_ROLE_3")
                .build());

        Collection<Role> resultRoles = roleRepository.findByNameIn(List.of("TEST_ROLE_1", "TEST_ROLE_3"));


        Assertions.assertThat(resultRoles).isNotNull();
        Assertions.assertThat(resultRoles).isNotEmpty();
        Assertions.assertThat(resultRoles).isEqualTo(List.of(role1, role3));
    }
}
