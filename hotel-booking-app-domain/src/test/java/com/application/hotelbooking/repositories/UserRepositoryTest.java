package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.Role;
import com.application.hotelbooking.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveReturnsSavedUser(){
        Role role1 = Role.builder()
                .name("TEST_ROLE_1")
                .build();
        Role role2 = Role.builder()
                .name("TEST_ROLE_2")
                .build();
        User user = User.builder()
                .username("TEST_USER_NAME")
                .password("TEST_PASSWORD")
                .email("TEST_USER_EMAIL")
                .roles(List.of(role1, role2))
                .enabled(true)
                .locked(false)
                .build();

        User savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isNotNull();
        Assertions.assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(savedUser.getRoles()).isEqualTo(user.getRoles());
    }

    @Test
    public void testFindByUsernameReturnsOptionalOfUserWithProvidedUsername(){
        User user = userRepository.save(User.builder()
                .username("TEST_USER_NAME")
                .password("TEST_PASSWORD")
                .email("TEST_USER_EMAIL")
                .enabled(true)
                .locked(false)
                .build());

        Optional<User> resultUser = userRepository.findByUsername(user.getUsername());

        Assertions.assertThat(resultUser).isNotNull();
        Assertions.assertThat(resultUser).isNotEmpty();
        Assertions.assertThat(resultUser.get().getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(resultUser.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void testFindByUsernameReturnsEmptyOptionalWithNonexistentUsernameProvided(){

        Optional<User> resultUser = userRepository.findByUsername("NONEXISTENT");

        Assertions.assertThat(resultUser).isNotNull();
        Assertions.assertThat(resultUser).isEmpty();
    }

    @Test
    public void testFindByEmailReturnsOptionalOfUserWithProvidedEmail(){
        User user = userRepository.save(User.builder()
                .username("TEST_USER_NAME")
                .password("TEST_PASSWORD")
                .email("TEST_USER_EMAIL")
                .enabled(true)
                .locked(false)
                .build());

        Optional<User> resultUser = userRepository.findByEmail(user.getEmail());

        Assertions.assertThat(resultUser).isNotNull();
        Assertions.assertThat(resultUser).isNotEmpty();
        Assertions.assertThat(resultUser.get().getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(resultUser.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void testFindByEmailReturnsEmptyOptionalWithNonexistentEmailProvided(){

        Optional<User> resultUser = userRepository.findByEmail("NONEXISTENT");

        Assertions.assertThat(resultUser).isNotNull();
        Assertions.assertThat(resultUser).isEmpty();
    }
}
