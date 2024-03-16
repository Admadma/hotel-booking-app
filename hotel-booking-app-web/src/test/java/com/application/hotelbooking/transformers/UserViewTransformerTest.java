package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.domain.UserView;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserViewTransformerTest {

    public static final UserModel USER_MODEL = new UserModel();
    public static final Class<UserView> USER_VIEW_CLASS = UserView.class;
    public static final UserView USER_VIEW = new UserView();
    @InjectMocks
    private UserViewTransformer userViewTransformer;
    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testTransformToUserViewShouldReturnTransformedUserModel(){
        when(modelMapper.map(USER_MODEL, USER_VIEW_CLASS)).thenReturn(USER_VIEW);

        UserView resultUserView = userViewTransformer.transformToUserView(USER_MODEL);

        verify(modelMapper).map(USER_MODEL, USER_VIEW_CLASS);
        Assertions.assertThat(resultUserView).isNotNull();
        Assertions.assertThat(resultUserView).isEqualTo(USER_VIEW);
    }
}
