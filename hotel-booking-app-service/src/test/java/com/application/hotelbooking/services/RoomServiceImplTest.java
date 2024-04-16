package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.HotelModel;
import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.domain.RoomType;
import com.application.hotelbooking.dto.HotelWithReservableRoomsServiceDTO;
import com.application.hotelbooking.dto.ReservableRoomDTO;
import com.application.hotelbooking.dto.RoomSearchFormServiceDTO;
import com.application.hotelbooking.services.implementations.RoomServiceImpl;
import com.application.hotelbooking.services.repositoryservices.RoomRepositoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomServiceImplTest {

    private static final List<Long> ID_ROOMS_WITH_CONDITIONS = List.of(1l, 2l, 3l, 4l, 5l);
    private static final List<Long> ID_AVAILABLE_ROOMS = List.of(1l, 2l, 3l, 4l);
    public static final LocalDate LOCAL_DATE_NOW_PLUS_FIVE_DAYS = LocalDate.now().plusDays(5);
    public static final LocalDate LOCAL_DATE_NOW_PLUS_TEN_DAYS = LocalDate.now().plusDays(10);
    private static final RoomSearchFormServiceDTO ROOM_SEARCH_FORM_SERVICE_DTO = RoomSearchFormServiceDTO.builder()
            .startDate(LOCAL_DATE_NOW_PLUS_FIVE_DAYS)
            .endDate(LOCAL_DATE_NOW_PLUS_TEN_DAYS)
            .build();
    private static final String HOTEL_ONE_NAME = "Hotel_1";
    private static final String HOTEL_TWO_NAME = "Hotel_2";
    private static final String CITY = "Test city";
    private static final String IMAGE_NAME = "test_image.png";
    private static final HotelModel HOTEL_ONE = HotelModel.builder()
            .hotelName(HOTEL_ONE_NAME)
            .city(CITY)
            .imageName(IMAGE_NAME)
            .averageRating(0.0)
            .build();
    private static final HotelModel HOTEL_TWO = HotelModel.builder()
            .hotelName(HOTEL_TWO_NAME)
            .city(CITY)
            .imageName(IMAGE_NAME)
            .averageRating(0.0)
            .build();
    private static final Optional<RoomModel> AVAILABLE_ROOM_1_HOTEL_1 = Optional.of(RoomModel.builder()
            .roomNumber(1)
            .singleBeds(2)
            .doubleBeds(1)
            .roomType(RoomType.FAMILY_ROOM)
            .pricePerNight(100)
            .hotel(HOTEL_ONE)
            .build());
    private static final Optional<RoomModel> AVAILABLE_ROOM_2_HOTEL_1 = Optional.of(RoomModel.builder()
            .roomNumber(2)
            .singleBeds(2)
            .doubleBeds(1)
            .roomType(RoomType.FAMILY_ROOM)
            .pricePerNight(150)
            .hotel(HOTEL_ONE)
            .build());
    private static final Optional<RoomModel> AVAILABLE_ROOM_3_HOTEL_1 = Optional.of(RoomModel.builder()
            .roomNumber(3)
            .singleBeds(3)
            .doubleBeds(1)
            .roomType(RoomType.FAMILY_ROOM)
            .pricePerNight(150)
            .hotel(HOTEL_ONE)
            .build());

    private static final Optional<RoomModel> AVAILABLE_ROOM_1_HOTEL_2 = Optional.of(RoomModel.builder()
            .roomNumber(1)
            .singleBeds(2)
            .doubleBeds(1)
            .roomType(RoomType.FAMILY_ROOM)
            .pricePerNight(150)
            .hotel(HOTEL_TWO)
            .build());

    @InjectMocks
    private RoomServiceImpl roomService;

    @Mock
    private RoomRepositoryService roomRepositoryService;

    @Mock
    private ReservationService reservationService;

    @Mock
    private AvailableRoomsFilterService availableRoomsFilterService;

    @Test
    public void testIsEndDateAfterStartDateShouldReturnFalseIfStartDateIsAfterEndDate(){
        boolean result = roomService.isEndDateAfterStartDate(LOCAL_DATE_NOW_PLUS_TEN_DAYS, LOCAL_DATE_NOW_PLUS_FIVE_DAYS);

        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void testIsEndDateAfterStartDateShouldReturnFalseIfStartDateAndEndDateAreTheSame(){
        boolean result = roomService.isEndDateAfterStartDate(LOCAL_DATE_NOW_PLUS_FIVE_DAYS, LOCAL_DATE_NOW_PLUS_FIVE_DAYS);

        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void testIsEndDateAfterStartDateShouldReturnTrueIfStartDateIsBeforeEndDate(){
        boolean result = roomService.isEndDateAfterStartDate(LOCAL_DATE_NOW_PLUS_FIVE_DAYS, LOCAL_DATE_NOW_PLUS_TEN_DAYS);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void testSearchHotelsWithReservableRooms(){
        when(roomRepositoryService.getRoomsWithConditions(ROOM_SEARCH_FORM_SERVICE_DTO)).thenReturn(ID_ROOMS_WITH_CONDITIONS);
        when(availableRoomsFilterService.filterFreeRooms(ID_ROOMS_WITH_CONDITIONS, ROOM_SEARCH_FORM_SERVICE_DTO.getStartDate(), ROOM_SEARCH_FORM_SERVICE_DTO.getEndDate())).thenReturn(ID_AVAILABLE_ROOMS);
        when(roomRepositoryService.getRoomById(1l)).thenReturn(AVAILABLE_ROOM_1_HOTEL_1);
        when(roomRepositoryService.getRoomById(2l)).thenReturn(AVAILABLE_ROOM_2_HOTEL_1);
        when(roomRepositoryService.getRoomById(3l)).thenReturn(AVAILABLE_ROOM_3_HOTEL_1);
        when(roomRepositoryService.getRoomById(4l)).thenReturn(AVAILABLE_ROOM_1_HOTEL_2);

        List<HotelWithReservableRoomsServiceDTO> resultHotelWithReservableRoomsServiceDTOList = roomService.searchHotelsWithReservableRooms(ROOM_SEARCH_FORM_SERVICE_DTO);

        verify(roomRepositoryService).getRoomsWithConditions(ROOM_SEARCH_FORM_SERVICE_DTO);
        verify(availableRoomsFilterService).filterFreeRooms(ID_ROOMS_WITH_CONDITIONS, ROOM_SEARCH_FORM_SERVICE_DTO.getStartDate(), ROOM_SEARCH_FORM_SERVICE_DTO.getEndDate());
        verify(roomRepositoryService).getRoomById(1l);
        verify(roomRepositoryService).getRoomById(2l);
        verify(roomRepositoryService).getRoomById(3l);
        verify(roomRepositoryService).getRoomById(4l);

        // Checking that I have a list item for each available hotel
        Assertions.assertThat(resultHotelWithReservableRoomsServiceDTOList.size()).isEqualTo(2);
        Assertions.assertThat(resultHotelWithReservableRoomsServiceDTOList.get(0).getHotelName()).isEqualTo(HOTEL_ONE_NAME);
        Assertions.assertThat(resultHotelWithReservableRoomsServiceDTOList.get(1).getHotelName()).isEqualTo(HOTEL_TWO_NAME);
        // Hotel 1 has a UniqueReservableRoomOfHotelServiceDTO for each unique hotel.
        // Since Room 1 and 2 are considered to be equal (in terms of their important attributes) the result should not contain Room 2 because 1 is already added.
        // Room 3 should be added because it is different from Room 1
        // Room 1 of Hotel 2 should not be present either, because it belongs to another hotel
        Assertions.assertThat(resultHotelWithReservableRoomsServiceDTOList.get(0).getUniqueReservableRoomOfHotelServiceDTOList().size()).isEqualTo(2);
        Assertions.assertThat(resultHotelWithReservableRoomsServiceDTOList.get(0).getUniqueReservableRoomOfHotelServiceDTOList().get(0).getNumber()).isEqualTo(AVAILABLE_ROOM_1_HOTEL_1.get().getRoomNumber());
        Assertions.assertThat(resultHotelWithReservableRoomsServiceDTOList.get(0).getUniqueReservableRoomOfHotelServiceDTOList().get(1).getNumber()).isEqualTo(AVAILABLE_ROOM_3_HOTEL_1.get().getRoomNumber());
        Assertions.assertThat(resultHotelWithReservableRoomsServiceDTOList.get(0).getUniqueReservableRoomOfHotelServiceDTOList().contains(AVAILABLE_ROOM_2_HOTEL_1)).isEqualTo(false);
        Assertions.assertThat(resultHotelWithReservableRoomsServiceDTOList.get(0).getUniqueReservableRoomOfHotelServiceDTOList().contains(AVAILABLE_ROOM_1_HOTEL_2)).isEqualTo(false);
        // Check if Hotel 2 also contains it's 1 room and nothing else
        Assertions.assertThat(resultHotelWithReservableRoomsServiceDTOList.get(1).getUniqueReservableRoomOfHotelServiceDTOList().size()).isEqualTo(1);
        Assertions.assertThat(resultHotelWithReservableRoomsServiceDTOList.get(1).getUniqueReservableRoomOfHotelServiceDTOList().get(0).getNumber()).isEqualTo(AVAILABLE_ROOM_1_HOTEL_2.get().getRoomNumber());
        Assertions.assertThat(resultHotelWithReservableRoomsServiceDTOList.get(1).getUniqueReservableRoomOfHotelServiceDTOList().contains(AVAILABLE_ROOM_1_HOTEL_1)).isEqualTo(false);
        Assertions.assertThat(resultHotelWithReservableRoomsServiceDTOList.get(1).getUniqueReservableRoomOfHotelServiceDTOList().contains(AVAILABLE_ROOM_2_HOTEL_1)).isEqualTo(false);
        Assertions.assertThat(resultHotelWithReservableRoomsServiceDTOList.get(1).getUniqueReservableRoomOfHotelServiceDTOList().contains(AVAILABLE_ROOM_3_HOTEL_1)).isEqualTo(false);
    }
}
