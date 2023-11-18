package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.domain.User;
import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.repositories.UserRepository;
import com.application.hotelbooking.services.repositoryservices.implementations.UserRepositoryServiceImpl;
import com.application.hotelbooking.transformers.UserTransformer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryServiceImplTest {

    public static final String USER_NAME = "TestUsername";
    public static final String NONEXISTENT_USER_NAME = "NonexistentUsername";
    public static final String EMAIL = "test@email";
    public static final String NONEXISTENT_EMAIL = "Nonexistent@email";
    public static final Optional<User> FOUND_USER = Optional.of(User.builder().username(USER_NAME).email(EMAIL).build());
    public static final Optional<UserModel> TRANSFORMED_USER = Optional.of(UserModel.builder().username(USER_NAME).email(EMAIL).build());
    public static final Optional<User> EMPTY_USER = Optional.empty();
    public static final Optional<UserModel> EMPTY_TRANSFORMED_USER = Optional.empty();
    public static final User USER = User.builder().username(USER_NAME).email(EMAIL).build();
    public static final UserModel USER_MODEL = UserModel.builder().username(USER_NAME).email(EMAIL).build();


    @InjectMocks
    private UserRepositoryServiceImpl userRepositoryService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTransformer userTransformer;

    @Test
    public void testGetUserByNameShouldReturnOptionalOfUserModelIfUserExists(){
        when(userRepository.findByUsername(USER_NAME)).thenReturn(FOUND_USER);
        when(userTransformer.transformToOptionalUserModel(FOUND_USER)).thenReturn(TRANSFORMED_USER);

        Optional<UserModel> resultUser = userRepositoryService.getUserByName(USER_NAME);

        verify(userRepository).findByUsername(USER_NAME);
        verify(userTransformer).transformToOptionalUserModel(FOUND_USER);
        Assertions.assertThat(resultUser).isNotNull();
        Assertions.assertThat(resultUser).isNotEmpty();
        Assertions.assertThat(resultUser.get().getUsername()).isEqualTo(USER_NAME);
    }

    @Test
    public void testGetUserByNameShouldReturnEmptyOptionalIfUsernameDoesNotExist(){
        when(userRepository.findByUsername(NONEXISTENT_USER_NAME)).thenReturn(EMPTY_USER);
        when(userTransformer.transformToOptionalUserModel(EMPTY_USER)).thenReturn(EMPTY_TRANSFORMED_USER);

        Optional<UserModel> resultUser = userRepositoryService.getUserByName(NONEXISTENT_USER_NAME);

        verify(userRepository).findByUsername(NONEXISTENT_USER_NAME);
        verify(userTransformer).transformToOptionalUserModel(EMPTY_USER);
        Assertions.assertThat(resultUser).isNotNull();
        Assertions.assertThat(resultUser).isEmpty();
    }

    @Test
    public void testGetUserByEmailShouldReturnOptionalOfUserModelIfEmailExists(){
        when(userRepository.findByEmail(EMAIL)).thenReturn(FOUND_USER);
        when(userTransformer.transformToOptionalUserModel(FOUND_USER)).thenReturn(TRANSFORMED_USER);

        Optional<UserModel> resultUser = userRepositoryService.getUserByEmail(EMAIL);

        verify(userRepository).findByEmail(EMAIL);
        verify(userTransformer).transformToOptionalUserModel(FOUND_USER);
        Assertions.assertThat(resultUser).isNotNull();
        Assertions.assertThat(resultUser).isNotEmpty();
        Assertions.assertThat(resultUser.get().getEmail()).isEqualTo(EMAIL);
    }

    @Test
    public void testGetUserByEmailShouldReturnEmptyOptionalIfEmailDoesNotExist(){
        when(userRepository.findByEmail(NONEXISTENT_EMAIL)).thenReturn(EMPTY_USER);
        when(userTransformer.transformToOptionalUserModel(EMPTY_USER)).thenReturn(EMPTY_TRANSFORMED_USER);

        Optional<UserModel> resultUser = userRepositoryService.getUserByEmail(NONEXISTENT_EMAIL);

        verify(userRepository).findByEmail(NONEXISTENT_EMAIL);
        verify(userTransformer).transformToOptionalUserModel(EMPTY_USER);
        Assertions.assertThat(resultUser).isNotNull();
        Assertions.assertThat(resultUser).isEmpty();
    }

    @Test
    public void testSaveShouldReturnUserModelOfSavedUser() {
        when(userTransformer.transformToUser(USER_MODEL)).thenReturn(USER);
        when(userRepository.save(USER)).thenReturn(USER);
        when(userTransformer.transformToUserModel(USER)).thenReturn(USER_MODEL);

        UserModel savedUser = userRepositoryService.save(USER_MODEL);

        verify(userTransformer).transformToUser(USER_MODEL);
        verify(userRepository).save(USER);
        verify(userTransformer).transformToUserModel(USER);
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getUsername()).isEqualTo(USER_NAME);
    }
}
