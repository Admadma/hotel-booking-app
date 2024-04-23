package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.entities.ConfirmationToken;
import com.application.hotelbooking.models.ConfirmationTokenModel;
import com.application.hotelbooking.repositories.ConfirmationTokenRepository;
import com.application.hotelbooking.services.repositoryservices.implementations.ConfirmationTokenRepositoryServiceImpl;
import com.application.hotelbooking.transformers.ConfirmationTokenTransformer;
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
public class ConfirmationTokenRepositoryServiceImplTest {

    private static final String TEST_TOKEN = "Test_token";
    private static final ConfirmationToken CONFIRMATION_TOKEN = ConfirmationToken.builder().token(TEST_TOKEN).build();
    private static final Optional<ConfirmationToken> OPTIONAL_CONFIRMATION_TOKEN = Optional.of(CONFIRMATION_TOKEN);
    private static final Optional<ConfirmationToken> EMPTY_OPTIONAL_CONFIRMATION_TOKEN = Optional.empty();
    private static final ConfirmationTokenModel CONFIRMATION_TOKEN_MODEL = ConfirmationTokenModel.builder().token(TEST_TOKEN).build();
    private static final Optional<ConfirmationTokenModel> OPTIONAL_CONFIRMATION_TOKEN_MODEL = Optional.of(CONFIRMATION_TOKEN_MODEL);
    private static final Optional<ConfirmationTokenModel> EMPTY_OPTIONAL_CONFIRMATION_TOKEN_MODEL = Optional.empty();
    @InjectMocks
    private ConfirmationTokenRepositoryServiceImpl confirmationTokenRepositoryService;

    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Mock
    private ConfirmationTokenTransformer confirmationTokenTransformer;


    @Test
    public void testSaveConfirmationTokenShouldReturnConfirmationTokenModelOfSavedConfirmationToken(){
        when(confirmationTokenTransformer.transformToConfirmationToken(CONFIRMATION_TOKEN_MODEL)).thenReturn(CONFIRMATION_TOKEN);
        when(confirmationTokenRepository.save(CONFIRMATION_TOKEN)).thenReturn(CONFIRMATION_TOKEN);
        when(confirmationTokenTransformer.transformToConfirmationTokenModel(CONFIRMATION_TOKEN)).thenReturn(CONFIRMATION_TOKEN_MODEL);

        ConfirmationTokenModel savedToken = confirmationTokenRepositoryService.saveConfirmationToken(CONFIRMATION_TOKEN_MODEL);

        verify(confirmationTokenTransformer).transformToConfirmationToken(CONFIRMATION_TOKEN_MODEL);
        verify(confirmationTokenRepository).save(CONFIRMATION_TOKEN);
        verify(confirmationTokenTransformer).transformToConfirmationTokenModel(CONFIRMATION_TOKEN);
        Assertions.assertThat(savedToken).isNotNull();
        Assertions.assertThat(savedToken.getToken()).isEqualTo(TEST_TOKEN);
    }
    @Test
    public void testGetByTokenShouldReturnOptionalOfConfirmationTokenModelIfConfirmationTokenExists(){
        when(confirmationTokenRepository.findByToken(TEST_TOKEN)).thenReturn(OPTIONAL_CONFIRMATION_TOKEN);
        when(confirmationTokenTransformer.transformToOptionalConfirmationTokenModel(OPTIONAL_CONFIRMATION_TOKEN)).thenReturn(OPTIONAL_CONFIRMATION_TOKEN_MODEL);

        Optional<ConfirmationTokenModel> resultToken = confirmationTokenRepositoryService.getByToken(TEST_TOKEN);

        verify(confirmationTokenRepository).findByToken(TEST_TOKEN);
        verify(confirmationTokenTransformer).transformToOptionalConfirmationTokenModel(OPTIONAL_CONFIRMATION_TOKEN);
        Assertions.assertThat(resultToken).isNotNull();
        Assertions.assertThat(resultToken).isNotEmpty();
        Assertions.assertThat(resultToken.get().getToken()).isEqualTo(TEST_TOKEN);
    }

    @Test
    public void testGetByTokenShouldReturnEmptyOptionalIfConfirmationTokenDoesNotExist(){
        when(confirmationTokenRepository.findByToken(TEST_TOKEN)).thenReturn(EMPTY_OPTIONAL_CONFIRMATION_TOKEN);
        when(confirmationTokenTransformer.transformToOptionalConfirmationTokenModel(EMPTY_OPTIONAL_CONFIRMATION_TOKEN)).thenReturn(EMPTY_OPTIONAL_CONFIRMATION_TOKEN_MODEL);

        Optional<ConfirmationTokenModel> resultToken = confirmationTokenRepositoryService.getByToken(TEST_TOKEN);

        verify(confirmationTokenRepository).findByToken(TEST_TOKEN);
        verify(confirmationTokenTransformer).transformToOptionalConfirmationTokenModel(EMPTY_OPTIONAL_CONFIRMATION_TOKEN);
        Assertions.assertThat(resultToken).isNotNull();
        Assertions.assertThat(resultToken).isEmpty();
    }
}
