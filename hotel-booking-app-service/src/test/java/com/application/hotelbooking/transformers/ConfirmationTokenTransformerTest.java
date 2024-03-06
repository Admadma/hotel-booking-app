package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.ConfirmationToken;
import com.application.hotelbooking.domain.ConfirmationTokenModel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConfirmationTokenTransformerTest {

    public static final Class<ConfirmationToken> CONFIRMATION_TOKEN_CLASS = ConfirmationToken.class;
    public static final ConfirmationToken CONFIRMATION_TOKEN = new ConfirmationToken();
    public static final Optional<ConfirmationToken> OPTIONAL_CONFIRMATION_TOKEN= Optional.of(CONFIRMATION_TOKEN);
    public static final Optional<ConfirmationToken> EMPTY_CONFIRMATION_TOKEN = Optional.empty();
    public static final Class<ConfirmationTokenModel> CONFIRMATION_TOKEN_MODEL_CLASS = ConfirmationTokenModel.class;
    public static final ConfirmationTokenModel CONFIRMATION_TOKEN_MODEL = new ConfirmationTokenModel();
    public static final Optional<ConfirmationTokenModel> OPTIONAL_CONFIRMATION_TOKEN_MODEL = Optional.of(CONFIRMATION_TOKEN_MODEL);

    @InjectMocks
    private ConfirmationTokenTransformer confirmationTokenTransformer;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testTransformToConfirmationTokenShouldReturnTransformedConfirmationTokenModel(){
        when(modelMapper.map(CONFIRMATION_TOKEN_MODEL, CONFIRMATION_TOKEN_CLASS)).thenReturn(CONFIRMATION_TOKEN);

        ConfirmationToken resultConfirmationToken = confirmationTokenTransformer.transformToConfirmationToken(CONFIRMATION_TOKEN_MODEL);

        verify(modelMapper).map(CONFIRMATION_TOKEN_MODEL, CONFIRMATION_TOKEN_CLASS);
        Assertions.assertThat(resultConfirmationToken).isNotNull();
        Assertions.assertThat(resultConfirmationToken).isEqualTo(CONFIRMATION_TOKEN);
    }

    @Test
    public void testTransformToConfirmationTokenModelShouldReturnTransformedConfirmationToken(){
        when(modelMapper.map(CONFIRMATION_TOKEN, CONFIRMATION_TOKEN_MODEL_CLASS)).thenReturn(CONFIRMATION_TOKEN_MODEL);

        ConfirmationTokenModel resultConfirmationTokenModel = confirmationTokenTransformer.transformToConfirmationTokenModel(CONFIRMATION_TOKEN);

        verify(modelMapper).map(CONFIRMATION_TOKEN, CONFIRMATION_TOKEN_MODEL_CLASS);
        Assertions.assertThat(resultConfirmationTokenModel).isNotNull();
        Assertions.assertThat(resultConfirmationTokenModel).isEqualTo(CONFIRMATION_TOKEN_MODEL);
    }

    @Test
    public void testTransformToOptionalConfirmationTokenModelShouldReturnOptionalOfPresentConfirmationToken(){
        when(modelMapper.map(OPTIONAL_CONFIRMATION_TOKEN, CONFIRMATION_TOKEN_MODEL_CLASS)).thenReturn(CONFIRMATION_TOKEN_MODEL);

        Optional<ConfirmationTokenModel> resultConfirmationTokenModel = confirmationTokenTransformer.transformToOptionalConfirmationTokenModel(OPTIONAL_CONFIRMATION_TOKEN);

        verify(modelMapper).map(OPTIONAL_CONFIRMATION_TOKEN, CONFIRMATION_TOKEN_MODEL_CLASS);
        Assertions.assertThat(resultConfirmationTokenModel).isNotNull();
        Assertions.assertThat(resultConfirmationTokenModel).isNotEmpty();
        Assertions.assertThat(resultConfirmationTokenModel).isEqualTo(OPTIONAL_CONFIRMATION_TOKEN_MODEL);
    }

    @Test
    public void testTransformToOptionalConfirmationTokenModelShouldReturnEmptyOptionalOfEmptyConfirmationToken(){

        Optional<ConfirmationTokenModel> resultConfirmationTokenModel = confirmationTokenTransformer.transformToOptionalConfirmationTokenModel(EMPTY_CONFIRMATION_TOKEN);

        Assertions.assertThat(resultConfirmationTokenModel).isNotNull();
        Assertions.assertThat(resultConfirmationTokenModel).isEmpty();
    }
}
