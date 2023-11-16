package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
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
    public void testFindHotelByHotelNameReturnsOptionalOfHotelWithProvidedName(){
        Role role = roleRepository.save(Role.builder()
                .name("TEST_USER")
                .build());

        Optional<Role> resultRole = roleRepository.findRoleByName("TEST_USER");

        Assertions.assertThat(resultRole).isNotNull();
        Assertions.assertThat(resultRole).isNotEmpty();
        Assertions.assertThat(resultRole.get()).isEqualTo(role);
    }

    @Test
    public void testFindHotelByHotelNameReturnsEmptyOptionalWithNonexistentNameProvided(){

        Optional<Role> resultRole = roleRepository.findRoleByName("TEST_USER");

        Assertions.assertThat(resultRole).isNotNull();
        Assertions.assertThat(resultRole).isEmpty();
    }

}
