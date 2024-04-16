package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.HotelModel;
import com.application.hotelbooking.domain.ReviewModel;
import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.dto.HotelCreationServiceDTO;
import com.application.hotelbooking.exceptions.InvalidHotelException;
import com.application.hotelbooking.services.implementations.HotelServiceImpl;
import com.application.hotelbooking.services.repositoryservices.HotelRepositoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HotelServiceImplTest {

    public static final long HOTEL_ID = 1l;
    public static final String HOTEL_NAME = "Existing hotel";
    public static final ReviewModel REVIEW_MODEL_ONE = ReviewModel.builder().rating(2).build();
    public static final ReviewModel REVIEW_MODEL_TWO = ReviewModel.builder().rating(5).build();
    public static final HotelModel HOTEL_MODEL = HotelModel.builder().rooms(List.of()).averageRating(0.0).reviews(List.of(REVIEW_MODEL_ONE, REVIEW_MODEL_TWO)).build();
    public static final HotelModel HOTEL_MODEL_WITH_UPDATED_AVERAGE_REVIEWS = HotelModel.builder().rooms(List.of()).averageRating(3.5).reviews(List.of(REVIEW_MODEL_ONE, REVIEW_MODEL_TWO)).build();
    public static final HotelModel HOTEL_MODEL_WITH_ROOMS = HotelModel.builder().rooms(List.of(RoomModel.builder().roomNumber(1).build())).build();
    public static final HotelCreationServiceDTO HOTEL_CREATION_SERVICE_DTO = HotelCreationServiceDTO.builder().hotelName(HOTEL_NAME).build();
    public static final Optional<HotelModel> OPTIONAL_HOTEL_MODEL = Optional.of(HOTEL_MODEL);
    public static final Optional<HotelModel> OPTIONAL_HOTEL_MODEL_WITH_ROOMS = Optional.of(HOTEL_MODEL_WITH_ROOMS);
    public static final Optional<HotelModel> EMPTY_HOTEL_MODEL = Optional.empty();

    @InjectMocks
    private HotelServiceImpl hotelService;

    @Mock
    private HotelRepositoryService hotelRepositoryService;

    @Test
    public void testCreateHotelShouldCallSaveWhenHotelNameIsFree(){
        when(hotelRepositoryService.findHotelByHotelName(HOTEL_NAME)).thenReturn(EMPTY_HOTEL_MODEL);
        when(hotelRepositoryService.create(HOTEL_CREATION_SERVICE_DTO)).thenReturn(HOTEL_MODEL);

        hotelService.createHotel(HOTEL_CREATION_SERVICE_DTO);

        verify(hotelRepositoryService).findHotelByHotelName(HOTEL_NAME);
        verify(hotelRepositoryService).create(HOTEL_CREATION_SERVICE_DTO);
    }

    @Test
    public void testCreateHotelShouldThrowInvalidHotelExceptionIfHotelNameAlreadyTaken(){
        when(hotelRepositoryService.findHotelByHotelName(HOTEL_NAME)).thenReturn(OPTIONAL_HOTEL_MODEL);

        Assertions.assertThatThrownBy(() -> hotelService.createHotel(HOTEL_CREATION_SERVICE_DTO))
                        .isInstanceOf(InvalidHotelException.class)
                                .hasMessage("That hotelName is already taken");
        verify(hotelRepositoryService).findHotelByHotelName(HOTEL_NAME);
    }

    @Test
    public void testGetLatestRoomNumberOfHotelShouldReturnRoomNumberOfLatestRoomAddedToHotel(){
        when(hotelRepositoryService.getHotelById(HOTEL_ID)).thenReturn(OPTIONAL_HOTEL_MODEL_WITH_ROOMS);

        int latestRoomNumberOfHotel = hotelService.getLatestRoomNumberOfHotel(HOTEL_ID);

        verify(hotelRepositoryService).getHotelById(HOTEL_ID);
        Assertions.assertThat(latestRoomNumberOfHotel).isEqualTo(1);
    }

    @Test
    public void testGetLatestRoomNumberOfHotelShouldReturnZeroIfHotelHasNoRooms(){
        when(hotelRepositoryService.getHotelById(HOTEL_ID)).thenReturn(OPTIONAL_HOTEL_MODEL);

        int latestRoomNumberOfHotel = hotelService.getLatestRoomNumberOfHotel(HOTEL_ID);

        verify(hotelRepositoryService).getHotelById(HOTEL_ID);
        Assertions.assertThat(latestRoomNumberOfHotel).isEqualTo(0);
    }

    @Test
    public void testUpdateAverageRatingShould(){
        when(hotelRepositoryService.save(HOTEL_MODEL_WITH_UPDATED_AVERAGE_REVIEWS)).thenReturn(HOTEL_MODEL_WITH_UPDATED_AVERAGE_REVIEWS);

        hotelService.updateAverageRating(HOTEL_MODEL);

        verify(hotelRepositoryService).save(HOTEL_MODEL_WITH_UPDATED_AVERAGE_REVIEWS);
    }
}
