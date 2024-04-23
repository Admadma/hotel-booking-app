package com.application.hotelbooking.repositories;

import com.application.hotelbooking.entities.Role;
import com.application.hotelbooking.entities.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class UserRepositoryTest {

    private static final Role ROLE = Role.builder()
            .name("TEST_ROLE_1")
            .build();
    private static final String NONEXISTENT = "NONEXISTENT";
    private static final String TEST_USER_NAME = "TEST_USER_NAME";
    private static final String TEST_PASSWORD = "TEST_PASSWORD";
    private static final String TEST_USER_EMAIL = "TEST_USER_EMAIL";

    private Role SAVED_ROLE;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        SAVED_ROLE = roleRepository.save(ROLE);
    }

    @Test
    public void testSaveReturnsSavedUser(){
        User user = User.builder()
                .username(TEST_USER_NAME)
                .password(TEST_PASSWORD)
                .email(TEST_USER_EMAIL)
                .roles(List.of(SAVED_ROLE))
                .enabled(true)
                .locked(false)
                .build();

        User savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isNotNull();
        Assertions.assertThat(savedUser.getUsername()).isEqualTo(TEST_USER_NAME);
        Assertions.assertThat(savedUser.getEmail()).isEqualTo(TEST_USER_EMAIL);
        Assertions.assertThat(savedUser.getRoles().contains(SAVED_ROLE)).isEqualTo(true);
        Assertions.assertThat(savedUser.getRoles().size()).isEqualTo(1);
    }

    @Test
    public void testFindByUsernameReturnsOptionalOfUserWithProvidedUsername(){
        User user = userRepository.save(User.builder()
                .username(TEST_USER_NAME)
                .password(TEST_PASSWORD)
                .email(TEST_USER_EMAIL)
                .roles(List.of(SAVED_ROLE))
                .enabled(true)
                .locked(false)
                .build());

        Optional<User> resultUser = userRepository.findByUsername(TEST_USER_NAME);

        Assertions.assertThat(resultUser).isNotNull();
        Assertions.assertThat(resultUser).isNotEmpty();
        Assertions.assertThat(resultUser.get().getUsername()).isEqualTo(TEST_USER_NAME);
        Assertions.assertThat(resultUser.get().getEmail()).isEqualTo(TEST_USER_EMAIL);
    }

    @Test
    public void testFindByUsernameReturnsEmptyOptionalWithNonexistentUsernameProvided(){

        Optional<User> resultUser = userRepository.findByUsername(NONEXISTENT);

        Assertions.assertThat(resultUser).isNotNull();
        Assertions.assertThat(resultUser).isEmpty();
    }

    @Test
    public void testFindByEmailReturnsOptionalOfUserWithProvidedEmail(){
        User user = userRepository.save(User.builder()
                .username(TEST_USER_NAME)
                .password(TEST_PASSWORD)
                .email(TEST_USER_EMAIL)
                .roles(List.of(SAVED_ROLE))
                .enabled(true)
                .locked(false)
                .build());

        Optional<User> resultUser = userRepository.findByEmail(TEST_USER_EMAIL);

        Assertions.assertThat(resultUser).isNotNull();
        Assertions.assertThat(resultUser).isNotEmpty();
        Assertions.assertThat(resultUser.get().getUsername()).isEqualTo(TEST_USER_NAME);
        Assertions.assertThat(resultUser.get().getEmail()).isEqualTo(TEST_USER_EMAIL);
    }

    @Test
    public void testFindByEmailReturnsEmptyOptionalWithNonexistentEmailProvided(){

        Optional<User> resultUser = userRepository.findByEmail(NONEXISTENT);

        Assertions.assertThat(resultUser).isNotNull();
        Assertions.assertThat(resultUser).isEmpty();
    }

    @Test
    public void testExistsByUsernameShouldReturnTrueIfUsernameExists(){
        userRepository.save(User.builder()
                .username(TEST_USER_NAME)
                .password(TEST_PASSWORD)
                .email(TEST_USER_EMAIL)
                .roles(List.of(SAVED_ROLE))
                .enabled(true)
                .locked(false)
                .build());

        boolean resultExists = userRepository.existsByUsername(TEST_USER_NAME);

        Assertions.assertThat(resultExists).isNotNull();
        Assertions.assertThat(resultExists).isTrue();
    }

    @Test
    public void testExistsByUsernameShouldReturnFalseIfUsernameDoesNotExist(){

        boolean resultExists = userRepository.existsByUsername(TEST_USER_NAME);

        Assertions.assertThat(resultExists).isNotNull();
        Assertions.assertThat(resultExists).isFalse();
    }

    @Test
    public void testExistsByEmailShouldReturnTrueIfEmailExists(){
        userRepository.save(User.builder()
                .username(TEST_USER_NAME)
                .password(TEST_PASSWORD)
                .email(TEST_USER_EMAIL)
                .roles(List.of(SAVED_ROLE))
                .enabled(true)
                .locked(false)
                .build());

        boolean resultExists = userRepository.existsByEmail(TEST_USER_EMAIL);

        Assertions.assertThat(resultExists).isNotNull();
        Assertions.assertThat(resultExists).isTrue();
    }

    @Test
    public void testExistsByUsEmailShouldReturnFalseIfEmailDoesNotExist(){

        boolean resultExists = userRepository.existsByEmail(TEST_USER_EMAIL);

        Assertions.assertThat(resultExists).isNotNull();
        Assertions.assertThat(resultExists).isFalse();
    }
}
