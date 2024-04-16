package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.HotelModel;
import com.application.hotelbooking.domain.ReviewModel;
import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.services.implementations.ReviewServiceImpl;
import com.application.hotelbooking.services.repositoryservices.HotelRepositoryService;
import com.application.hotelbooking.services.repositoryservices.ReviewRepositoryService;
import com.application.hotelbooking.services.repositoryservices.UserRepositoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest {

    private static final String HOTEL_NAME = "Hotel 1";
    private static final HotelModel HOTEL_MODEL = HotelModel.builder().hotelName(HOTEL_NAME).build();
    private static final Optional<HotelModel> OPTIONAL_HOTEL_MODEL = Optional.of(HOTEL_MODEL);
    private static final String USER_NAME = "User 1";
    private static final UserModel USER_MODEL = UserModel.builder().username(USER_NAME).build();
    private static final Optional<UserModel> OPTIONAL_USER_MODEL = Optional.of(USER_MODEL);
    private static final int RATING = 5;
    private static final String COMMENT = "Some comment";
    private static final ReviewModel REVIEW_MODEL = ReviewModel.builder()
            .rating(RATING)
            .comment(COMMENT)
            .hotel(HOTEL_MODEL)
            .user(USER_MODEL)
            .build();

    @InjectMocks
    private ReviewServiceImpl reviewServiceImpl;

    @Mock
    private ReviewRepositoryService reviewRepositoryService;

    @Mock
    private HotelRepositoryService hotelRepositoryService;

    @Mock
    private UserRepositoryService userRepositoryService;

    @Mock
    private HotelService hotelService;

    @Test
    public void testSearchHotelsWithReservableRoomsShouldSearchForAvailableRoomsMatchingTheSearchConditionAndReturnTheUniqueOnesGroupedByHotel(){
        when(hotelRepositoryService.findHotelByHotelName(HOTEL_NAME)).thenReturn(OPTIONAL_HOTEL_MODEL);
        when(userRepositoryService.getUserByName(USER_NAME)).thenReturn(OPTIONAL_USER_MODEL);
        when(reviewRepositoryService.save(REVIEW_MODEL)).thenReturn(REVIEW_MODEL);
        doNothing().when(hotelService).updateAverageRating(HOTEL_MODEL);

        ReviewModel resultReviewModel = reviewServiceImpl.createReview(RATING, COMMENT, HOTEL_NAME, USER_NAME);

        verify(hotelRepositoryService).findHotelByHotelName(HOTEL_NAME);
        verify(userRepositoryService).getUserByName(USER_NAME);
        verify(reviewRepositoryService).save(REVIEW_MODEL);
        verify(hotelService).updateAverageRating(HOTEL_MODEL);
    }
}
