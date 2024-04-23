package com.application.hotelbooking.security;

import com.application.hotelbooking.models.UserModel;
import com.application.hotelbooking.domain.UserView;
import com.application.hotelbooking.services.repositoryservices.UserRepositoryService;
import com.application.hotelbooking.transformers.UserViewTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailServiceTest {

    private static final String USERNAME = "username";
    private static final UserModel USER_MODEL = UserModel.builder().username(USERNAME).build();
    private static final UserView USER_VIEW = new UserView();
    private static final Optional<UserModel> OPTIONAL_USER_MODEL = Optional.of(USER_MODEL);
    private static final Optional<UserModel> OPTIONAL_USER_MODEL_EMPTY = Optional.empty();

    @InjectMocks
    private MyUserDetailService myUserDetailService;

    @Mock
    private UserRepositoryService userRepositoryService;

    @Mock
    private UserViewTransformer userViewTransformer;

    @Test
    public void testLoadUserByUsernameShouldReturnUserDetailsOfFoundUser(){
        when(userRepositoryService.getUserByName(USERNAME)).thenReturn(OPTIONAL_USER_MODEL);
        when(userViewTransformer.transformToUserView(USER_MODEL)).thenReturn(USER_VIEW);

        UserDetails resultMyUserDetails = myUserDetailService.loadUserByUsername(USERNAME);

        verify(userRepositoryService, times(2)).getUserByName(USERNAME);
        verify(userViewTransformer).transformToUserView(USER_MODEL);
    }

    @Test
    public void testLoadUserByUsernameShouldThrowUsernameNotFoundExceptionIfUserNotFound(){
        when(userRepositoryService.getUserByName(USERNAME)).thenReturn(OPTIONAL_USER_MODEL_EMPTY);

        assertThrows(UsernameNotFoundException.class,
                () -> myUserDetailService.loadUserByUsername(USERNAME));

        verify(userRepositoryService).getUserByName(USERNAME);
    }
}
