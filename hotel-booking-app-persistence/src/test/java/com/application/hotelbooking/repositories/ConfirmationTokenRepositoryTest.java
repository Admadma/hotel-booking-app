package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.ConfirmationToken;
import com.application.hotelbooking.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
public class ConfirmationTokenRepositoryTest {

    private static final User USER = User.builder()
            .username("TEST_USER_NAME")
            .password("TEST_PASSWORD")
            .email("TEST_USER_EMAIL")
            .enabled(true)
            .locked(false)
            .build();
    private static final String TEST_TOKEN = "TEST-TOKEN";
    private static final String NONEXISTENT_TOKEN = "NONEXISTENT-TOKEN";
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2024, 3, 1, 0, 0);
    private static final LocalDateTime EXPIRES_AT = LocalDateTime.of(2024, 3, 1, 0, 30);

    private User SAVED_USER;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SAVED_USER = userRepository.save(USER);
    }

    @Test
    public void testSaveReturnsSavedConfirmationToken(){
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(TEST_TOKEN)
                .user(SAVED_USER)
                .createdAt(CREATED_AT)
                .expiresAt(EXPIRES_AT)
                .build();

        ConfirmationToken savedToken = confirmationTokenRepository.save(confirmationToken);

        Assertions.assertThat(savedToken).isNotNull();
        Assertions.assertThat(savedToken.getId()).isNotNull();
        Assertions.assertThat(savedToken.getToken()).isEqualTo(confirmationToken.getToken());
    }

    @Test
    public void testFindByTokenReturnsOptionalOfConfirmationTokenWithProvidedToken(){
        ConfirmationToken confirmationToken = confirmationTokenRepository.save(ConfirmationToken.builder()
                .token(TEST_TOKEN)
                .user(SAVED_USER)
                .createdAt(CREATED_AT)
                .expiresAt(EXPIRES_AT)
                .build());


        Optional<ConfirmationToken> resultConfirmationToken = confirmationTokenRepository.findByToken(confirmationToken.getToken());

        Assertions.assertThat(resultConfirmationToken).isNotNull();
        Assertions.assertThat(resultConfirmationToken).isNotEmpty();
        Assertions.assertThat(resultConfirmationToken.get().getToken()).isEqualTo(confirmationToken.getToken());
    }

    @Test
    public void testFindByIdReturnsEmptyOptionalWithNonexistentIdProvided(){

        Optional<ConfirmationToken> resultConfirmationToken = confirmationTokenRepository.findByToken(NONEXISTENT_TOKEN);

        Assertions.assertThat(resultConfirmationToken).isNotNull();
        Assertions.assertThat(resultConfirmationToken).isEmpty();
    }
}
