package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.*;
import com.application.hotelbooking.dto.HotelWithReservableRoomsServiceDTO;
import com.application.hotelbooking.dto.ReservableRoomDTO;
import com.application.hotelbooking.dto.ReservationPlanServiceDTO;
import com.application.hotelbooking.dto.UniqueReservableRoomOfHotelServiceDTO;
import com.application.hotelbooking.exceptions.InvalidReservationException;
import com.application.hotelbooking.exceptions.InvalidTokenException;
import com.application.hotelbooking.exceptions.InvalidUserException;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTest {

    private static final Long USER_ID = 1l;
    private static final long VERSION = 0l;
    private static final UUID TEST_UUID = UUID.fromString("2a167ea9-850c-4059-8163-6f941561c419");
    private static final String USER_NAME = "Test user";
    private static final String HOTEL_NAME = "Test hotel";
    private static final String HOTEL_NAME_TWO = "Test hotel_2";
    private static final String CITY = "Test city";
    private static final String IMAGE_NAME = "test_image.png";
    private static final Double AVERAGE_RATING = 5.0;
    private static final long RESERVATION_ID = 1l;
    private static final List<Long> ID_ROOMS_WITH_CONDITIONS = List.of(1l, 2l, 3l, 4l, 5l);
    private static final List<Long> ID_AVAILABLE_ROOMS = List.of(1l, 4l);
    private static final LocalDate OBSERVED_START_DATE = LocalDate.of(2024, 3, 1);
    private static final LocalDate OBSERVED_END_DATE = LocalDate.of(2024, 3, 5);
    private static final UserModel USER_MODEL = UserModel.builder()
            .id(USER_ID)
            .username(USER_NAME)
            .build();
    private static final UserModel USER_MODEL_WITH_DIFFERENT_USER_NAME = UserModel.builder()
            .id(USER_ID)
            .username("Different name")
            .build();
    private static final Optional<UserModel> OPTIONAL_USER_MODEL = Optional.of(USER_MODEL);
    private static final List<ReservationModel> RESERVATION_MODELS = List.of(ReservationModel.builder()
            .startDate(OBSERVED_START_DATE)
            .endDate(OBSERVED_END_DATE)
            .build());
    private static final ReservableRoomDTO RESERVABLE_ROOM_DTO = ReservableRoomDTO.builder()
            .roomNumber(1)
            .hotelName(HOTEL_NAME)
            .totalPrice(100)
            .startDate(OBSERVED_START_DATE)
            .endDate(OBSERVED_END_DATE)
            .build();

    private static final ReservationPlanServiceDTO RESERVATION_PLAN = ReservationPlanServiceDTO.builder()
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
    private static final HotelModel HOTEL_MODEL = HotelModel.builder()
            .hotelName(HOTEL_NAME)
            .city(CITY)
            .build();
    private static final RoomModel ROOM_MODEL = RoomModel.builder()
            .roomNumber(1)
            .pricePerNight(RESERVATION_PLAN.getPricePerNight())
            .hotel(HOTEL_MODEL)
            .version(VERSION)
            .build();
    private static final RoomModel ROOM_MODEL_PRICE_PER_NIGHT_20 = RoomModel.builder()
            .roomNumber(2)
            .hotel(HOTEL_MODEL)
            .pricePerNight(20)
            .version(VERSION)
            .build();
    private static final Optional<RoomModel> OPTIONAL_ROOM_MODEL = Optional.of(ROOM_MODEL);
    private static final Optional<RoomModel> OPTIONAL_ROOM_MODEL_PRICE_PER_NIGHT_20 = Optional.of(ROOM_MODEL_PRICE_PER_NIGHT_20);
    private static final ReservationModel RESERVATION_MODEL = ReservationModel.builder()
            .uuid(TEST_UUID)
            .room(ROOM_MODEL)
            .user(USER_MODEL)
            .totalPrice(RESERVATION_PLAN.getTotalPrice())
            .startDate(OBSERVED_START_DATE)
            .endDate(OBSERVED_END_DATE)
            .reservationStatus(ReservationStatus.PLANNED)
            .build();

    private static final Optional<ReservationModel> OPTIONAL_RESERVATION_MODEL = Optional.of(ReservationModel.builder()
            .id(RESERVATION_ID)
            .uuid(TEST_UUID)
            .user(USER_MODEL)
            .reservationStatus(ReservationStatus.PLANNED)
            .build());
    private static final Optional<ReservationModel> OPTIONAL_RESERVATION_MODEL_ACTIVE = Optional.of(ReservationModel.builder()
            .uuid(TEST_UUID)
            .room(ROOM_MODEL)
            .user(USER_MODEL)
            .totalPrice(RESERVATION_PLAN.getTotalPrice())
            .startDate(OBSERVED_START_DATE)
            .endDate(OBSERVED_END_DATE)
            .reservationStatus(ReservationStatus.ACTIVE)
            .build());
    private static final Optional<ReservationModel> OPTIONAL_RESERVATION_MODEL_PLANNED_DIFFERENT_USERNAME = Optional.of(ReservationModel.builder()
            .uuid(TEST_UUID)
            .user(USER_MODEL_WITH_DIFFERENT_USER_NAME)
            .reservationStatus(ReservationStatus.PLANNED)
            .build());
    private static final Optional<ReservationModel> EMPTY_OPTIONAL_RESERVATION_MODEL = Optional.empty();

    private static UniqueReservableRoomOfHotelServiceDTO UNIQUE_RESERVABLE_ROOM_OF_HOTEL_SERVICE_DTO_ONE = UniqueReservableRoomOfHotelServiceDTO.builder()
            .number(1)
            .singleBeds(1)
            .doubleBeds(0)
            .pricePerNight(10)
            .totalPrice(40)
            .roomType(RoomType.SINGLE_ROOM)
            .startDate(OBSERVED_START_DATE)
            .endDate(OBSERVED_END_DATE)
            .build();
    private static UniqueReservableRoomOfHotelServiceDTO UNIQUE_RESERVABLE_ROOM_OF_HOTEL_SERVICE_DTO_TWO = UniqueReservableRoomOfHotelServiceDTO.builder()
            .number(2)
            .singleBeds(2)
            .doubleBeds(2)
            .pricePerNight(10)
            .totalPrice(40)
            .roomType(RoomType.FAMILY_ROOM)
            .startDate(OBSERVED_START_DATE)
            .endDate(OBSERVED_END_DATE)
            .build();
    private static UniqueReservableRoomOfHotelServiceDTO UNIQUE_RESERVABLE_ROOM_OF_HOTEL_SERVICE_DTO_THREE = UniqueReservableRoomOfHotelServiceDTO.builder()
            .number(1)
            .singleBeds(2)
            .doubleBeds(2)
            .pricePerNight(10)
            .totalPrice(40)
            .roomType(RoomType.FAMILY_ROOM)
            .startDate(OBSERVED_START_DATE)
            .endDate(OBSERVED_END_DATE)
            .build();

    private static HotelWithReservableRoomsServiceDTO HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_ONE = HotelWithReservableRoomsServiceDTO.builder()
            .hotelName(HOTEL_NAME)
            .city(CITY)
            .imageName(IMAGE_NAME)
            .averageRating(AVERAGE_RATING)
            .uniqueReservableRoomOfHotelServiceDTOList(List.of(UNIQUE_RESERVABLE_ROOM_OF_HOTEL_SERVICE_DTO_ONE, UNIQUE_RESERVABLE_ROOM_OF_HOTEL_SERVICE_DTO_TWO))
            .build();

    private static HotelWithReservableRoomsServiceDTO HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_TWO = HotelWithReservableRoomsServiceDTO.builder()
            .hotelName(HOTEL_NAME_TWO)
            .city(CITY)
            .imageName(IMAGE_NAME)
            .averageRating(AVERAGE_RATING)
            .uniqueReservableRoomOfHotelServiceDTOList(List.of(UNIQUE_RESERVABLE_ROOM_OF_HOTEL_SERVICE_DTO_THREE))
            .build();

    private static List<HotelWithReservableRoomsServiceDTO> HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST = List.of(HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_ONE, HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_TWO);

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private UserRepositoryService userRepositoryService;

    @Mock
    private ReservationRepositoryService reservationRepositoryService;

    @Mock
    private AvailableRoomsFilterService availableRoomsFilterService;

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
    public void testCancelReservationShouldThrowInvalidTokenExceptionIfNoReservationExistsWithProvidedUUID() {
        when(reservationRepositoryService.getReservationByUuid(TEST_UUID)).thenReturn(EMPTY_OPTIONAL_RESERVATION_MODEL);

        assertThrows(InvalidTokenException.class,
                () -> reservationService.cancelReservation(TEST_UUID, USER_NAME));

        verify(reservationRepositoryService).getReservationByUuid(TEST_UUID);
    }

    @Test
    public void testCancelReservationShouldThrowInvalidReservationExceptionIfFoundReservationIsNotInPlannedStatus() {
        when(reservationRepositoryService.getReservationByUuid(TEST_UUID)).thenReturn(OPTIONAL_RESERVATION_MODEL_ACTIVE);

        assertThrows(InvalidReservationException.class,
                () -> reservationService.cancelReservation(TEST_UUID, USER_NAME));

        verify(reservationRepositoryService).getReservationByUuid(TEST_UUID);
    }

    @Test
    public void testCancelReservationShouldThrowInvalidUserExceptionIfFoundReservationIsInPlannedStatusButUserNameDoesNotMatch() {
        when(reservationRepositoryService.getReservationByUuid(TEST_UUID)).thenReturn(OPTIONAL_RESERVATION_MODEL_PLANNED_DIFFERENT_USERNAME);

        assertThrows(InvalidUserException.class,
                () -> reservationService.cancelReservation(TEST_UUID, USER_NAME));

        verify(reservationRepositoryService).getReservationByUuid(TEST_UUID);
    }

    @Test
    public void testCancelReservationShouldNotThrowExceptionAndDeleteReservationOfUser() {
        when(reservationRepositoryService.getReservationByUuid(TEST_UUID)).thenReturn(OPTIONAL_RESERVATION_MODEL);
        doNothing().when(reservationRepositoryService).delete(RESERVATION_ID);

        assertDoesNotThrow(() -> reservationService.cancelReservation(TEST_UUID, USER_NAME));

        verify(reservationRepositoryService).getReservationByUuid(TEST_UUID);
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

    @Test
    public void testReserveRoomShouldSearchForAvailableRoomsMatchingTheSelectedSearchConditionAndSaveTheFirstMatchingResultAndSendEmailOfReservationAndReturnReservationModelOfSavedReservationPlan(){
        when(roomRepositoryService.getRoomsWithConditions(RESERVATION_PLAN.getSingleBeds(), RESERVATION_PLAN.getDoubleBeds(), RESERVATION_PLAN.getRoomType(), RESERVATION_PLAN.getHotelName(), RESERVATION_PLAN.getCity())).thenReturn(ID_ROOMS_WITH_CONDITIONS);
        when(availableRoomsFilterService.filterFreeRooms(ID_ROOMS_WITH_CONDITIONS, RESERVATION_PLAN.getStartDate(), RESERVATION_PLAN.getEndDate())).thenReturn(ID_AVAILABLE_ROOMS);
        when(roomRepositoryService.getRoomById(ID_AVAILABLE_ROOMS.get(0))).thenReturn(OPTIONAL_ROOM_MODEL_PRICE_PER_NIGHT_20);
        when(roomRepositoryService.getRoomById(ID_AVAILABLE_ROOMS.get(1))).thenReturn(OPTIONAL_ROOM_MODEL);
        when(uuidWrapper.getRandomUUID()).thenReturn(TEST_UUID);
        when(userRepositoryService.getUserByName(USER_NAME)).thenReturn(OPTIONAL_USER_MODEL);
        when(reservationRepositoryService.save(RESERVATION_MODEL)).thenReturn(RESERVATION_MODEL);
        doNothing().when(roomRepositoryService).incrementRoomVersion(ROOM_MODEL);
        doNothing().when(reservationConfirmationEmailService).sendReservationConfirmationEmail(RESERVATION_MODEL);

        ReservationModel resultReservationModel = reservationService.reserveRoom(RESERVATION_PLAN, USER_NAME);

        verify(roomRepositoryService).getRoomsWithConditions(RESERVATION_PLAN.getSingleBeds(), RESERVATION_PLAN.getDoubleBeds(), RESERVATION_PLAN.getRoomType(), RESERVATION_PLAN.getHotelName(), RESERVATION_PLAN.getCity());
        verify(availableRoomsFilterService).filterFreeRooms(ID_ROOMS_WITH_CONDITIONS, RESERVATION_PLAN.getStartDate(), RESERVATION_PLAN.getEndDate());
        verify(roomRepositoryService).getRoomById(ID_AVAILABLE_ROOMS.get(0));
        verify(roomRepositoryService).getRoomById(ID_AVAILABLE_ROOMS.get(1));
        verify(uuidWrapper).getRandomUUID();
        verify(userRepositoryService).getUserByName(USER_NAME);
        verify(reservationRepositoryService).save(RESERVATION_MODEL);
        verify(roomRepositoryService).incrementRoomVersion(ROOM_MODEL);
        verify(reservationConfirmationEmailService).sendReservationConfirmationEmail(RESERVATION_MODEL);
        Assertions.assertThat(resultReservationModel).isNotNull();
        Assertions.assertThat(resultReservationModel.getRoom().getRoomNumber()).isEqualTo(RESERVATION_MODEL.getRoom().getRoomNumber());
    }

    @Test
    public void testReserveRoomShouldThrowOutdatedReservationExceptionIfNoRoomsMatchTheSelectedSearchConditions(){
        when(roomRepositoryService.getRoomsWithConditions(RESERVATION_PLAN.getSingleBeds(), RESERVATION_PLAN.getDoubleBeds(), RESERVATION_PLAN.getRoomType(), RESERVATION_PLAN.getHotelName(), RESERVATION_PLAN.getCity())).thenReturn(ID_ROOMS_WITH_CONDITIONS);
        when(availableRoomsFilterService.filterFreeRooms(ID_ROOMS_WITH_CONDITIONS, RESERVATION_PLAN.getStartDate(), RESERVATION_PLAN.getEndDate())).thenReturn(ID_AVAILABLE_ROOMS);
        when(roomRepositoryService.getRoomById(ID_AVAILABLE_ROOMS.get(0))).thenReturn(OPTIONAL_ROOM_MODEL_PRICE_PER_NIGHT_20);
        when(roomRepositoryService.getRoomById(ID_AVAILABLE_ROOMS.get(1))).thenReturn(OPTIONAL_ROOM_MODEL_PRICE_PER_NIGHT_20);

        assertThrows(OutdatedReservationException.class,
                () -> reservationService.reserveRoom(RESERVATION_PLAN, USER_NAME));

        verify(roomRepositoryService).getRoomsWithConditions(RESERVATION_PLAN.getSingleBeds(), RESERVATION_PLAN.getDoubleBeds(), RESERVATION_PLAN.getRoomType(), RESERVATION_PLAN.getHotelName(), RESERVATION_PLAN.getCity());
        verify(availableRoomsFilterService).filterFreeRooms(ID_ROOMS_WITH_CONDITIONS, RESERVATION_PLAN.getStartDate(), RESERVATION_PLAN.getEndDate());
        verify(roomRepositoryService).getRoomById(ID_AVAILABLE_ROOMS.get(0));
        verify(roomRepositoryService).getRoomById(ID_AVAILABLE_ROOMS.get(1));
    }

    @Test
    public void testCreateReservationPlanShouldExtractTheHotelAndRoomDetailsOfHotelWithReservableRoomsServiceDTOListAndReturnTheCreatedReservationPlan(){

        ReservationPlanServiceDTO reservationPlanServiceDTOHotelOneRoomOne = reservationService.createReservationPlan(1, HOTEL_NAME, HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST);
        ReservationPlanServiceDTO reservationPlanServiceDTOHotelOneRoomTwo = reservationService.createReservationPlan(2, HOTEL_NAME, HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST);
        ReservationPlanServiceDTO reservationPlanServiceDTOHotelTwoRoomOne = reservationService.createReservationPlan(1, HOTEL_NAME_TWO, HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST);

        Assertions.assertThat(reservationPlanServiceDTOHotelOneRoomOne.getHotelName()).isEqualTo(HOTEL_NAME);
        Assertions.assertThat(reservationPlanServiceDTOHotelOneRoomOne.getPricePerNight()).isEqualTo(UNIQUE_RESERVABLE_ROOM_OF_HOTEL_SERVICE_DTO_ONE.getPricePerNight());
        Assertions.assertThat(reservationPlanServiceDTOHotelOneRoomTwo.getHotelName()).isEqualTo(HOTEL_NAME);
        Assertions.assertThat(reservationPlanServiceDTOHotelOneRoomTwo.getPricePerNight()).isEqualTo(UNIQUE_RESERVABLE_ROOM_OF_HOTEL_SERVICE_DTO_TWO.getPricePerNight());
        Assertions.assertThat(reservationPlanServiceDTOHotelTwoRoomOne.getHotelName()).isEqualTo(HOTEL_NAME_TWO);
        Assertions.assertThat(reservationPlanServiceDTOHotelTwoRoomOne.getPricePerNight()).isEqualTo(UNIQUE_RESERVABLE_ROOM_OF_HOTEL_SERVICE_DTO_THREE.getPricePerNight());
    }
}
