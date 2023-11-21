package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.RoleModel;
import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.exceptions.CredentialMismatchException;
import com.application.hotelbooking.exceptions.EmailAlreadyExistsException;
import com.application.hotelbooking.exceptions.InvalidUserException;
import com.application.hotelbooking.exceptions.UserAlreadyExistsException;
import com.application.hotelbooking.services.implementations.UserEmailConfirmationServiceImpl;
import com.application.hotelbooking.services.implementations.UserServiceImpl;
import com.application.hotelbooking.services.repositoryservices.RoleRepositoryService;
import com.application.hotelbooking.services.repositoryservices.UserRepositoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    public static final String TEST_USERNAME = "Test_user";
    public static final String TEST_PASSWORD = "Test_password";
    public static final String INCORRECT_OLD_PASSWORD = "incorrect_old_password";
    public static final String TEST_EMAIL = "Test_email";
    public static final List<String> USER_ROLES_STRINGS = List.of("USER");
    public static final List<RoleModel> USER_ROLE_MODELS = List.of(RoleModel.builder().name("USER").build());
    public static final List<String> ADMIN_ROLES_STRINGS = List.of("ADMIN");
    public static final List<RoleModel> ADMIN_ROLE_MODELS = List.of(RoleModel.builder().name("ADMIN").build());
    public static final UserModel USER_USER_MODEL = UserModel.builder()
            .username(TEST_USERNAME)
            .password(TEST_PASSWORD)
            .email(TEST_EMAIL)
            .enabled(false)
            .roles(USER_ROLE_MODELS)
            .build();
    public static final UserModel ADMIN_USER_MODEL = UserModel.builder()
            .username(TEST_USERNAME)
            .password(TEST_PASSWORD)
            .email(TEST_EMAIL)
            .enabled(true)
            .roles(ADMIN_ROLE_MODELS)
            .build();
    public static final Optional<UserModel> OPTIONAL_USER_MODEL = Optional.of(UserModel.builder().username(TEST_USERNAME).password(TEST_PASSWORD).build());
    public static final Optional<UserModel> EMPTY_OPTIONAL_USER_MODEL = Optional.empty();
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepositoryService userRepositoryService;

    @Mock
    private UserEmailConfirmationServiceImpl userEmailConfirmationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepositoryService roleRepositoryService;

    @Test
    public void testCreateUserShouldThrowExceptionIfUsernameTaken(){
        when(userRepositoryService.userExists(TEST_USERNAME)).thenReturn(true);

        Assertions.assertThatThrownBy(() -> userService.createUser(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL, USER_ROLES_STRINGS))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("That username is already taken");

        verify(userRepositoryService).userExists(TEST_USERNAME);
    }

    @Test
    public void testCreateUserShouldThrowExceptionIfUsernameFreeButEmailTaken(){
        when(userRepositoryService.userExists(TEST_USERNAME)).thenReturn(false);
        when(userRepositoryService.emailExists(TEST_EMAIL)).thenReturn(true);

        Assertions.assertThatThrownBy(() -> userService.createUser(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL, USER_ROLES_STRINGS))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("That email is already taken");

        verify(userRepositoryService).userExists(TEST_USERNAME);
        verify(userRepositoryService).emailExists(TEST_EMAIL);
    }

    @Test
    public void testCreateUserShouldNotSendConfirmationTokenAndReturnUserModelOfSavedAdminIfRolesContainsAdmin(){
        when(userRepositoryService.userExists(TEST_USERNAME)).thenReturn(false);
        when(userRepositoryService.emailExists(TEST_EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(TEST_PASSWORD);
        when(roleRepositoryService.getRoles(ADMIN_ROLES_STRINGS)).thenReturn(ADMIN_ROLE_MODELS);
        when(userRepositoryService.save(ADMIN_USER_MODEL)).thenReturn(ADMIN_USER_MODEL);

        UserModel resultUserModel = userService.createUser(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL, ADMIN_ROLES_STRINGS);

        verify(userRepositoryService).userExists(TEST_USERNAME);
        verify(userRepositoryService).emailExists(TEST_EMAIL);
        verify(passwordEncoder).encode(TEST_PASSWORD);
        verify(roleRepositoryService).getRoles(ADMIN_ROLES_STRINGS);
        verify(userRepositoryService).save(ADMIN_USER_MODEL);
        Assertions.assertThat(resultUserModel).isNotNull();
    }

    @Test
    public void testCreateUserShouldSendConfirmationTokenAndReturnUserModelOfSavedUserIfRolesDoesNotContainAdmin(){
        when(userRepositoryService.userExists(TEST_USERNAME)).thenReturn(false);
        when(userRepositoryService.emailExists(TEST_EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(TEST_PASSWORD);
        when(roleRepositoryService.getRoles(USER_ROLES_STRINGS)).thenReturn(USER_ROLE_MODELS);
        when(userRepositoryService.save(USER_USER_MODEL)).thenReturn(USER_USER_MODEL);
        doNothing().when(userEmailConfirmationService).sendConfirmationToken(TEST_USERNAME, TEST_EMAIL);

        UserModel resultUserModel = userService.createUser(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL, USER_ROLES_STRINGS);

        verify(userRepositoryService).userExists(TEST_USERNAME);
        verify(userRepositoryService).emailExists(TEST_EMAIL);
        verify(passwordEncoder).encode(TEST_PASSWORD);
        verify(roleRepositoryService).getRoles(USER_ROLES_STRINGS);
        verify(userRepositoryService).save(USER_USER_MODEL);
        verify(userEmailConfirmationService).sendConfirmationToken(TEST_USERNAME, TEST_EMAIL);
        Assertions.assertThat(resultUserModel).isNotNull();
    }

    @Test
    public void testChangePasswordShouldThrowInvalidUserExceptionIfUserNotFound(){
        when(userRepositoryService.getUserByName(TEST_USERNAME)).thenReturn(EMPTY_OPTIONAL_USER_MODEL);

        Assertions.assertThatThrownBy(() -> userService.changePassword(TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("User does not exist");

        verify(userRepositoryService).getUserByName(TEST_USERNAME);
    }

    @Test
    public void testChangePasswordShouldThrowCredentialMismatchExceptionIfProvidedOldPasswordDoesNotMatchExistingCurrentPassword(){
        when(userRepositoryService.getUserByName(TEST_USERNAME)).thenReturn(OPTIONAL_USER_MODEL);
        when(passwordEncoder.matches(INCORRECT_OLD_PASSWORD, OPTIONAL_USER_MODEL.get().getPassword())).thenReturn(false);

        Assertions.assertThatThrownBy(() -> userService.changePassword(TEST_USERNAME, TEST_PASSWORD, INCORRECT_OLD_PASSWORD))
                .isInstanceOf(CredentialMismatchException.class)
                .hasMessage("The provided old password does not match.");

        verify(userRepositoryService, times(2)).getUserByName(TEST_USERNAME);
        verify(passwordEncoder).matches(INCORRECT_OLD_PASSWORD, OPTIONAL_USER_MODEL.get().getPassword());
    }

    @Test
    public void testChangePasswordShouldSaveUserModelWithUpdatedPassword(){
        when(userRepositoryService.getUserByName(TEST_USERNAME)).thenReturn(OPTIONAL_USER_MODEL);
        when(passwordEncoder.matches(TEST_PASSWORD, OPTIONAL_USER_MODEL.get().getPassword())).thenReturn(true);
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(TEST_PASSWORD);
        when(userRepositoryService.save(OPTIONAL_USER_MODEL.get())).thenReturn(OPTIONAL_USER_MODEL.get());

        userService.changePassword(TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD);

        verify(userRepositoryService, times(2)).getUserByName(TEST_USERNAME);
        verify(passwordEncoder).matches(TEST_PASSWORD, OPTIONAL_USER_MODEL.get().getPassword());
        verify(passwordEncoder).encode(TEST_PASSWORD);
        verify(userRepositoryService).save(OPTIONAL_USER_MODEL.get());
    }
}
