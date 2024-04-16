package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.*;
import com.application.hotelbooking.dto.ReservableRoomDTO;
import com.application.hotelbooking.dto.ReservationPlanServiceDTO;
import com.application.hotelbooking.exceptions.OutdatedReservationException;
import com.application.hotelbooking.services.implementations.ReservationConfirmationEmailServiceImpl;
import com.application.hotelbooking.services.implementations.ReservationServiceImpl;
import com.application.hotelbooking.services.repositoryservices.ReservationRepositoryService;
import com.application.hotelbooking.services.repositoryservices.RoomRepositoryService;
import com.application.hotelbooking.services.repositoryservices.UserRepositoryService;
import com.application.hotelbooking.wrappers.UUIDWrapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTest {

    public static final Long USER_ID = 1l;
    public static final long VERSION = 0l;
    private static final UUID TEST_UUID = UUID.fromString("2a167ea9-850c-4059-8163-6f941561c419");
    public static final String USER_NAME = "Test user";
    public static final String HOTEL_NAME = "Test hotel";
    public static final String CITY = "Test city";
    public static final long ROOM_ID_1 = 1l;
    public static final long ROOM_ID_2 = 2l;
    public static final long ROOM_ID_3 = 3l;
    public static final long ROOM_ID_4 = 4l;
    public static final long RESERVATION_ID = 1l;
    private static final List<Long> ID_ROOMS_WITH_CONDITIONS = List.of(1l, 2l, 3l, 4l, 5l);
    private static final List<Long> ID_AVAILABLE_ROOMS = List.of(1l, 4l);
    public static final LocalDate OBSERVED_START_DATE = LocalDate.of(2024, 3, 1);
    public static final LocalDate OBSERVED_END_DATE = LocalDate.of(2024, 3, 5);
    public static final LocalDate OBSERVED_START_DATE_PLUS_ONE = OBSERVED_START_DATE.plusDays(1);
    public static final LocalDate OBSERVED_END_DATE_MINUS_ONE = OBSERVED_END_DATE.minusDays(1);
    public static final LocalDate OBSERVED_START_DATE_MINUS_9 = OBSERVED_START_DATE.minusDays(9);
    public static final LocalDate OBSERVED_START_DATE_MINUS_10 = OBSERVED_START_DATE.minusDays(10);
    public static final LocalDate OBSERVED_END_DATE_PLUS_9 = OBSERVED_END_DATE.plusDays(9);
    public static final LocalDate OBSERVED_END_DATE_PLUS_10 = OBSERVED_END_DATE.plusDays(10);
    public static final Optional<UserModel> OPTIONAL_USER_MODEL = Optional.of(UserModel.builder()
            .id(USER_ID)
            .username(USER_NAME)
            .build());
    public static final List<ReservationModel> RESERVATION_MODELS = List.of(ReservationModel.builder()
            .startDate(OBSERVED_START_DATE)
            .endDate(OBSERVED_END_DATE)
            .build());
    public static final ReservableRoomDTO RESERVABLE_ROOM_DTO = ReservableRoomDTO.builder()
            .roomNumber(1)
            .hotelName(HOTEL_NAME)
            .totalPrice(100)
            .startDate(OBSERVED_START_DATE)
            .endDate(OBSERVED_END_DATE)
            .build();

    public static final ReservationPlanServiceDTO RESERVATION_PLAN = ReservationPlanServiceDTO.builder()
            .hotelName(HOTEL_NAME)
            .city(CITY)
            .roomType(RoomType.FAMILY_ROOM)
            .singleBeds(2)
            .doubleBeds(2)
            .startDate(OBSERVED_START_DATE)
            .endDate(OBSERVED_END_DATE)
            .pricePerNight(10)
            .totalPrice(40)
            .build();
    private static final RoomModel ROOM_MODEL = RoomModel.builder()
            .roomNumber(1)
            .version(VERSION)
            .build();
    private static final Optional<RoomModel> OPTIONAL_ROOM_MODEL = Optional.of(ROOM_MODEL);
    public static final Optional<RoomModel> OPTIONAL_ROOM_MODEL_WITH_CONFLICTING_RESERVATION_AND_INCREMENTED_ID = Optional.of(RoomModel.builder()
            .roomNumber(1)
            .reservations(List.of(ReservationModel.builder()
                    .startDate(OBSERVED_START_DATE)
                    .endDate(OBSERVED_END_DATE)
                    .build()))
            .version(VERSION + 1)
            .build());
    public static final Optional<RoomModel> OPTIONAL_ROOM_MODEL_WITH_NON_CONFLICTING_RESERVATION_AND_INCREMENTED_ID = Optional.of(RoomModel.builder()
            .roomNumber(1)
            .reservations(List.of(ReservationModel.builder()
                    .startDate(OBSERVED_START_DATE_MINUS_9)
                    .endDate(OBSERVED_START_DATE_MINUS_10)
                    .build()))
            .version(VERSION + 1)
            .build());
    public static final ReservationModel RESERVATION_MODEL = ReservationModel.builder()
            .room(RoomModel.builder()
                    .roomNumber(1)
                    .hotel(HotelModel.builder()
                            .hotelName(HOTEL_NAME)
                            .build())
                    .version(VERSION)
                    .build())
            .startDate(OBSERVED_START_DATE)
            .endDate(OBSERVED_END_DATE)
            .build();
    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private UserRepositoryService userRepositoryService;

    @Mock
    private ReservationRepositoryService reservationRepositoryService;

    @Mock
    private RoomRepositoryService roomRepositoryService;

    @Mock
    private ReservationConfirmationEmailServiceImpl reservationConfirmationEmailService;

    @Mock
    private UUIDWrapper uuidWrapper;

    @Test
    public void testGetReservationsOfUserShouldReturnListOfReservationModelsBelongingToUser(){
        when(userRepositoryService.getUserByName(USER_NAME)).thenReturn(OPTIONAL_USER_MODEL);
        when(reservationRepositoryService.getReservationsByUserId(USER_ID)).thenReturn(RESERVATION_MODELS);

        List<ReservationModel> resultReservationModels = reservationService.getReservationsOfUser(USER_NAME);

        verify(userRepositoryService).getUserByName(USER_NAME);
        verify(reservationRepositoryService).getReservationsByUserId(USER_ID);
        Assertions.assertThat(resultReservationModels).isNotNull();
        Assertions.assertThat(resultReservationModels).isNotEmpty();
        Assertions.assertThat(resultReservationModels).isEqualTo(RESERVATION_MODELS);
    }

    @Test
    public void testCancelReservationShouldCallDeleteWithProvidedId() {
        doNothing().when(reservationRepositoryService).delete(RESERVATION_ID);

        reservationService.cancelReservation(RESERVATION_ID);

        verify(reservationRepositoryService).delete(RESERVATION_ID);
    }

    @Test
    public void testCalculateTotalPriceShouldReturnPricePerNightMultipliedByDifferenceOfStartAndEndDate(){
        int totalPrice = reservationService.calculateTotalPrice(OBSERVED_START_DATE, OBSERVED_END_DATE, 10);

        Assertions.assertThat(totalPrice).isEqualTo(40);
    }

    @Test
    public void testPrepareReservationShouldReturnReservationModelOfPreparedReservation(){
        when(uuidWrapper.getRandomUUID()).thenReturn(TEST_UUID);
        when(userRepositoryService.getUserByName(USER_NAME)).thenReturn(OPTIONAL_USER_MODEL);

        ReservationModel resultReservationModel = reservationService.prepareReservation(RESERVATION_PLAN, ROOM_MODEL, USER_NAME);

        verify(uuidWrapper).getRandomUUID();
        verify(userRepositoryService).getUserByName(USER_NAME);
        Assertions.assertThat(resultReservationModel).isNotNull();
        Assertions.assertThat(resultReservationModel.getRoom().getRoomNumber()).isEqualTo(RESERVABLE_ROOM_DTO.getRoomNumber());
        Assertions.assertThat(resultReservationModel.getUser().getUsername()).isEqualTo(USER_NAME);
        Assertions.assertThat(resultReservationModel.getStartDate()).isEqualTo(RESERVABLE_ROOM_DTO.getStartDate());
    }

//    @Test
//    public void testReserveRoomShouldSearchForAvailableRoomsMatchingTheSelectedSearchConditionAndSaveTheFirstMatchingResultAndSendEmailOfReservationAndReturnReservationModelOfSavedReservationPlan(){
//        when(roomRepositoryService.getRoomsWithConditions(RESERVATION_PLAN.getSingleBeds(), RESERVATION_PLAN.getDoubleBeds(), RESERVATION_PLAN.getRoomType(), RESERVATION_PLAN.getHotelName(), RESERVATION_PLAN.getCity())).thenReturn(ID_ROOMS_WITH_CONDITIONS);
//        when(reservationRepositoryService.save(RESERVATION_MODEL)).thenReturn(RESERVATION_MODEL);
//        doNothing().when(roomRepositoryService).incrementRoomVersion(RESERVATION_MODEL.getRoom());
//        doNothing().when(reservationConfirmationEmailService).sendReservationConfirmationEmail(RESERVATION_MODEL);
//
//        ReservationModel resultReservationModel = reservationService.reserveRoom(RESERVATION_MODEL);
//
//        verify(roomRepositoryService).findRoomByNumberAndHotelName(RESERVATION_MODEL.getRoom().getRoomNumber(), RESERVATION_MODEL.getRoom().getHotel().getHotelName());
//        verify(reservationRepositoryService).save(RESERVATION_MODEL);
//        verify(roomRepositoryService).incrementRoomVersion(RESERVATION_MODEL.getRoom());
//        verify(reservationConfirmationEmailService).sendReservationConfirmationEmail(RESERVATION_MODEL);
//        Assertions.assertThat(resultReservationModel).isNotNull();
//        Assertions.assertThat(resultReservationModel.getRoom().getRoomNumber()).isEqualTo(RESERVATION_MODEL.getRoom().getRoomNumber());
//    }
//
//
//    @Test
//    public void testReserveRoomShouldReturnReservationModelOfSavedReservationIfRoomVersionChangedButStillNoConflictingReservations(){
//        when(roomRepositoryService.findRoomByNumberAndHotelName(RESERVATION_MODEL.getRoom().getRoomNumber(), RESERVATION_MODEL.getRoom().getHotel().getHotelName())).thenReturn(OPTIONAL_ROOM_MODEL_WITH_NON_CONFLICTING_RESERVATION_AND_INCREMENTED_ID);
//        when(reservationRepositoryService.save(RESERVATION_MODEL)).thenReturn(RESERVATION_MODEL);
//        doNothing().when(roomRepositoryService).incrementRoomVersion(RESERVATION_MODEL.getRoom());
//        doNothing().when(reservationConfirmationEmailService).sendReservationConfirmationEmail(RESERVATION_MODEL);
//
//        ReservationModel resultReservationModel = reservationService.reserveRoom(RESERVATION_MODEL);
//
//        verify(roomRepositoryService, times(2)).findRoomByNumberAndHotelName(RESERVABLE_ROOM_DTO.getRoomNumber(), RESERVABLE_ROOM_DTO.getHotelName());
//        verify(reservationRepositoryService).save(RESERVATION_MODEL);
//        verify(roomRepositoryService).incrementRoomVersion(RESERVATION_MODEL.getRoom());
//        verify(reservationConfirmationEmailService).sendReservationConfirmationEmail(RESERVATION_MODEL);
//        Assertions.assertThat(resultReservationModel).isNotNull();
//        Assertions.assertThat(resultReservationModel.getRoom().getRoomNumber()).isEqualTo(RESERVABLE_ROOM_DTO.getRoomNumber());
//    }
//
//    @Test
//    public void testReserveRoomShouldThrowExceptionIfRoomVersionChangedAndHasConflictingReservations(){
//        when(roomRepositoryService.findRoomByNumberAndHotelName(RESERVATION_MODEL.getRoom().getRoomNumber(), RESERVATION_MODEL.getRoom().getHotel().getHotelName())).thenReturn(OPTIONAL_ROOM_MODEL_WITH_CONFLICTING_RESERVATION_AND_INCREMENTED_ID);
//
//        Assertions.assertThatThrownBy(() -> reservationService.reserveRoom(RESERVATION_MODEL))
//                .isInstanceOf(OutdatedReservationException.class)
//                .hasMessage("This reservation is no longer valid because the room has been updated");
//
//        verify(roomRepositoryService, times(2)).findRoomByNumberAndHotelName(RESERVABLE_ROOM_DTO.getRoomNumber(), RESERVABLE_ROOM_DTO.getHotelName());
//    }
}
