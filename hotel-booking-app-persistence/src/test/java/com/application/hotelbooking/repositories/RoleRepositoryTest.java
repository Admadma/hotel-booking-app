package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class RoleRepositoryTest {
    private static final String ROLE_ONE_NAME = "TEST_ROLE_ONE";
    private static final String ROLE_TWO_NAME = "TEST_ROLE_TWO";
    private static final String ROLE_THREE_NAME = "TEST_ROLE_THREE";

    private static final User USER_ONE = User.builder()
            .username("user_1")
            .password("password_1")
            .email("email_1")
            .locked(false)
            .enabled(true)
            .build();
    private static final User USER_TWO = User.builder()
            .username("user_2")
            .password("password_1")
            .email("email_2")
            .locked(false)
            .enabled(true)
            .build();

    private User SAVED_USER_ONE;
    private User SAVED_USER_TWO;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SAVED_USER_ONE = userRepository.save(USER_ONE);
        SAVED_USER_TWO = userRepository.save(USER_TWO);
    }

    @Test
    public void testSaveReturnsSavedRole(){
        Role role = Role.builder()
                .name(ROLE_ONE_NAME)
                .users(Arrays.asList(SAVED_USER_ONE, SAVED_USER_TWO))
                .build();

        Role savedRole = roleRepository.save(role);

        Assertions.assertThat(savedRole).isNotNull();
        Assertions.assertThat(savedRole.getId()).isNotNull();
        Assertions.assertThat(savedRole.getName()).isEqualTo(ROLE_ONE_NAME);
        Assertions.assertThat(savedRole.getUsers().size()).isEqualTo(2);
        Assertions.assertThat(savedRole.getUsers().contains(SAVED_USER_ONE)).isEqualTo(true);
        Assertions.assertThat(savedRole.getUsers().contains(SAVED_USER_TWO)).isEqualTo(true);
    }

    @Test
    public void testFindAllReturnsAllRoles(){
        Role role1 = roleRepository.save(Role.builder()
                .name(ROLE_ONE_NAME)
                .users(Arrays.asList(SAVED_USER_ONE))
                .build());
        Role role2 = roleRepository.save(Role.builder()
                .name(ROLE_TWO_NAME)
                .users(Arrays.asList(SAVED_USER_TWO))
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
                .name(ROLE_ONE_NAME)
                .users(Arrays.asList(SAVED_USER_ONE, SAVED_USER_TWO))
                .build());

        Optional<Role> resultRole = roleRepository.findRoleByName(ROLE_ONE_NAME);

        Assertions.assertThat(resultRole).isNotNull();
        Assertions.assertThat(resultRole).isNotEmpty();
        Assertions.assertThat(resultRole.get().getName()).isEqualTo(role.getName());
    }

    @Test
    public void testFindRoleByNameReturnsEmptyOptionalWithNonexistentNameProvided(){

        Optional<Role> resultRole = roleRepository.findRoleByName(ROLE_ONE_NAME);

        Assertions.assertThat(resultRole).isNotNull();
        Assertions.assertThat(resultRole).isEmpty();
    }

    @Test
    public void testFindByRoleNameInShouldReturnCollectionOfRolesWhereRoleNameInList(){
        Role role1 = roleRepository.save(Role.builder()
                .name(ROLE_ONE_NAME)
                .users(Arrays.asList(SAVED_USER_ONE))
                .build());
        Role role2 = roleRepository.save(Role.builder()
                .name(ROLE_TWO_NAME)
                .users(Arrays.asList(SAVED_USER_TWO))
                .build());
        Role role3 = roleRepository.save(Role.builder()
                .name(ROLE_THREE_NAME)
                .users(Arrays.asList(SAVED_USER_ONE, SAVED_USER_TWO))
                .build());

        Collection<Role> resultRoles = roleRepository.findByNameIn(List.of(ROLE_ONE_NAME, ROLE_THREE_NAME));

        Assertions.assertThat(resultRoles).isNotNull();
        Assertions.assertThat(resultRoles).isNotEmpty();
        Assertions.assertThat(resultRoles).isEqualTo(List.of(role1, role3));
    }
}
