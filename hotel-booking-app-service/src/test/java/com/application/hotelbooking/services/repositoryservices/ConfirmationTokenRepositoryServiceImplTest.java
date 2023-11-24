package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.repositories.ConfirmationTokenRepository;
import com.application.hotelbooking.transformers.ConfirmationTokenTransformer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
public class ConfirmationTokenRepositoryServiceImplTest {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private ConfirmationTokenTransformer confirmationTokenTransformer;
}
