package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.*;
import com.application.hotelbooking.entities.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserTransformerTest {

    public static final Class<User> USER_CLASS = User.class;
    public static final User USER = new User();
    public static final Optional<User> OPTIONAL_USER = Optional.of(USER);
    public static final Optional<User> EMPTY_USER = Optional.empty();
    public static final Class<UserModel> USER_MODEL_CLASS = UserModel.class;
    public static final UserModel USER_MODEL = new UserModel();
    public static final Optional<UserModel> OPTIONAL_USER_MODEL = Optional.of(USER_MODEL);

    @InjectMocks
    private UserTransformer userTransformer;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testTransformToUserShouldReturnTransformedUserModel(){
        when(modelMapper.map(USER_MODEL, USER_CLASS)).thenReturn(USER);

        User resultUser = userTransformer.transformToUser(USER_MODEL);

        verify(modelMapper).map(USER_MODEL, USER_CLASS);
        Assertions.assertThat(resultUser).isNotNull();
        Assertions.assertThat(resultUser).isEqualTo(USER);
    }

    @Test
    public void testTransformToUserModelShouldReturnTransformedUser(){
        when(modelMapper.map(USER, USER_MODEL_CLASS)).thenReturn(USER_MODEL);

        UserModel resultUserModel = userTransformer.transformToUserModel(USER);

        verify(modelMapper).map(USER, USER_MODEL_CLASS);
        Assertions.assertThat(resultUserModel).isNotNull();
        Assertions.assertThat(resultUserModel).isEqualTo(USER_MODEL);
    }

    @Test
    public void testTransformToOptionalUserModelShouldReturnOptionalOfPresentUser(){
        when(modelMapper.map(OPTIONAL_USER, USER_MODEL_CLASS)).thenReturn(USER_MODEL);

        Optional<UserModel> resultUserModel = userTransformer.transformToOptionalUserModel(OPTIONAL_USER);

        verify(modelMapper).map(OPTIONAL_USER, USER_MODEL_CLASS);
        Assertions.assertThat(resultUserModel).isNotNull();
        Assertions.assertThat(resultUserModel).isNotEmpty();
        Assertions.assertThat(resultUserModel).isEqualTo(OPTIONAL_USER_MODEL);
    }

    @Test
    public void testTransformToOptionalRoomModelShouldReturnEmptyOptionalOfEmptyRoom(){

        Optional<UserModel> resultUserModel = userTransformer.transformToOptionalUserModel(EMPTY_USER);

        Assertions.assertThat(resultUserModel).isNotNull();
        Assertions.assertThat(resultUserModel).isEmpty();
    }
}
