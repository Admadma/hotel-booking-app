package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.ConfirmationToken;
import com.application.hotelbooking.domain.Hotel;
import com.application.hotelbooking.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
public class ConfirmationTokenRepositoryTest {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveReturnsSavedConfirmationToken(){
        User user = userRepository.save(User.builder()
                .username("TEST_USER_NAME")
                .password("TEST_PASSWORD")
                .email("TEST_USER_EMAIL")
                .enabled(true)
                .locked(false)
                .build());
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token("TEST-TOKEN")
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();

        ConfirmationToken savedToken = confirmationTokenRepository.save(confirmationToken);

        Assertions.assertThat(savedToken).isNotNull();
        Assertions.assertThat(savedToken.getId()).isNotNull();
        Assertions.assertThat(savedToken.getToken()).isEqualTo(confirmationToken.getToken());
    }

    @Test
    public void testFindByTokenReturnsOptionalOfConfirmationTokenWithProvidedToken(){
        User user = userRepository.save(User.builder()
                .username("TEST_USER_NAME")
                .password("TEST_PASSWORD")
                .email("TEST_USER_EMAIL")
                .enabled(true)
                .locked(false)
                .build());
        ConfirmationToken confirmationToken = confirmationTokenRepository.save(ConfirmationToken.builder()
                .token("TEST-TOKEN")
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build());


        Optional<ConfirmationToken> resultConfirmationToken = confirmationTokenRepository.findByToken(confirmationToken.getToken());

        Assertions.assertThat(resultConfirmationToken).isNotNull();
        Assertions.assertThat(resultConfirmationToken).isNotEmpty();
        Assertions.assertThat(resultConfirmationToken.get().getToken()).isEqualTo(confirmationToken.getToken());
    }

    @Test
    public void testFindByIdReturnsEmptyOptionalWithNonexistentIdProvided(){

        Optional<ConfirmationToken> resultConfirmationToken = confirmationTokenRepository.findByToken("NONEXISTENT-TOKEN");

        Assertions.assertThat(resultConfirmationToken).isNotNull();
        Assertions.assertThat(resultConfirmationToken).isEmpty();
    }
}
