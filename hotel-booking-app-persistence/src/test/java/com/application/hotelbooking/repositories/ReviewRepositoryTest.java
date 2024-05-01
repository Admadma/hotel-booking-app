package com.application.hotelbooking.repositories;

import com.application.hotelbooking.entities.Hotel;
import com.application.hotelbooking.entities.Review;
import com.application.hotelbooking.entities.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ReviewRepositoryTest {

    private static final Hotel HOTEL = Hotel.builder()
            .hotelName("Hotel 1")
            .city("City 1")
            .imageName("image.png")
            .averageRating(0.0)
            .build();
    private static final User USER = User.builder()
            .username("TEST_USER_NAME")
            .password("TEST_PASSWORD")
            .email("TEST_USER_EMAIL")
            .enabled(true)
            .locked(false)
            .build();
    private Hotel SAVED_HOTEL;
    private User SAVED_USER;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SAVED_HOTEL = hotelRepository.save(HOTEL);
        SAVED_USER = userRepository.save(USER);
    }

    @Test
    public void testSaveReturnsSavedReview(){
        Review review = Review.builder()
                .rating(5)
                .hotel(SAVED_HOTEL)
                .user(SAVED_USER)
                .build();

        Review savedReview = reviewRepository.save(review);

        Assertions.assertThat(savedReview).isNotNull();
        Assertions.assertThat(savedReview.getId()).isNotNull();
        Assertions.assertThat(savedReview.getRating()).isEqualTo(5);
        Assertions.assertThat(savedReview.getHotel().getId()).isEqualTo(SAVED_HOTEL.getId());
        Assertions.assertThat(savedReview.getUser().getId()).isEqualTo(SAVED_USER.getId());
    }
}
