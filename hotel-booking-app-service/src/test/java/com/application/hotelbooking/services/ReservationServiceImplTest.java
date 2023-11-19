package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.services.implementations.ReservationServiceImpl;
import com.application.hotelbooking.services.repositoryservices.ReservationRepositoryService;
import com.application.hotelbooking.services.repositoryservices.RoomRepositoryService;
import com.application.hotelbooking.services.repositoryservices.UserRepositoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTest {

    public static final Long USER_ID = 1l;
    public static final String USER_NAME = "Test user";
    public static final Optional<UserModel> OPTIONAL_USER_MODEL = Optional.of(UserModel.builder().id(USER_ID).username(USER_NAME).build());
    public static final List<ReservationModel> RESERVATION_MODELS = List.of(ReservationModel.builder().build());
    public static final long RESERVATION_ID = 1l;
    public static final LocalDate OBSERVED_START_DATE = LocalDate.now();
    public static final LocalDate OBSERVED_END_DATE = LocalDate.now().plusDays(4);
    public static final LocalDate OBSERVED_START_DATE_PLUS_ONE = OBSERVED_START_DATE.plusDays(1);
    public static final LocalDate OBSERVED_END_DATE_MINUS_ONE = OBSERVED_END_DATE.minusDays(1);
    public static final LocalDate OBSERVED_START_DATE_MINUS_9 = OBSERVED_START_DATE.minusDays(9);
    public static final LocalDate OBSERVED_START_DATE_MINUS_10 = OBSERVED_START_DATE.minusDays(10);
    public static final LocalDate OBSERVED_END_DATE_PLUS_9 = OBSERVED_END_DATE.plusDays(9);
    public static final LocalDate OBSERVED_END_DATE_PLUS_10 = OBSERVED_END_DATE.plusDays(10);
    public static final long ROOM_ID_1 = 1l;
    public static final long ROOM_ID_2 = 2l;
    public static final long ROOM_ID_3 = 3l;
    public static final long ROOM_ID_4 = 4l;
    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private UserRepositoryService userRepositoryService;

    @Mock
    private ReservationRepositoryService reservationRepositoryService;

    @Mock
    private RoomRepositoryService roomRepositoryService;

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private MessageSource messageSource;

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
    public void testFilterFreeRoomsShouldAllowRoomsWithNoReservations(){
        when(reservationRepositoryService.getReservationsByRoomId(ROOM_ID_1)).thenReturn(List.of());

        List<Long> resultRoomIds = reservationService.filterFreeRooms(List.of(ROOM_ID_1), OBSERVED_START_DATE, OBSERVED_END_DATE);

        verify(reservationRepositoryService).getReservationsByRoomId(ROOM_ID_1);
        Assertions.assertThat(resultRoomIds).isNotNull();
        Assertions.assertThat(resultRoomIds).isNotEmpty();
        Assertions.assertThat(resultRoomIds).isEqualTo(List.of(ROOM_ID_1));
    }
    @Test
    public void testFilterFreeRoomsShouldReturnEmptyListIfAllRoomsHaveConflictingReservations(){
//        This reservation starts before and ends during the observed reservation
        List<ReservationModel> reservationModels1 = List.of(ReservationModel.builder().startDate(OBSERVED_START_DATE_MINUS_10).endDate(OBSERVED_START_DATE_PLUS_ONE).build());
//        This reservation starts during and ends after the observed reservation
        List<ReservationModel> reservationModels2 = List.of(ReservationModel.builder().startDate(OBSERVED_END_DATE_MINUS_ONE).endDate(OBSERVED_END_DATE_PLUS_10).build());
//        This reservation starts and ends during the observed reservation
        List<ReservationModel> reservationModels3 = List.of(ReservationModel.builder().startDate(OBSERVED_START_DATE_PLUS_ONE).endDate(OBSERVED_END_DATE_MINUS_ONE).build());
//        This reservation starts before and ends after the observed reservation
        List<ReservationModel> reservationModels4 = List.of(ReservationModel.builder().startDate(OBSERVED_START_DATE_MINUS_10).endDate(OBSERVED_END_DATE_PLUS_10).build());

        when(reservationRepositoryService.getReservationsByRoomId(ROOM_ID_1)).thenReturn(reservationModels1);
        when(reservationRepositoryService.getReservationsByRoomId(ROOM_ID_2)).thenReturn(reservationModels2);
        when(reservationRepositoryService.getReservationsByRoomId(ROOM_ID_3)).thenReturn(reservationModels3);
        when(reservationRepositoryService.getReservationsByRoomId(ROOM_ID_4)).thenReturn(reservationModels4);

        List<Long> resultRoomIds = reservationService.filterFreeRooms(List.of(ROOM_ID_1, ROOM_ID_2, ROOM_ID_3, ROOM_ID_4), OBSERVED_START_DATE, OBSERVED_END_DATE);

        verify(reservationRepositoryService).getReservationsByRoomId(ROOM_ID_1);
        verify(reservationRepositoryService).getReservationsByRoomId(ROOM_ID_2);
        verify(reservationRepositoryService).getReservationsByRoomId(ROOM_ID_3);
        verify(reservationRepositoryService).getReservationsByRoomId(ROOM_ID_4);
        Assertions.assertThat(resultRoomIds).isNotNull();
        Assertions.assertThat(resultRoomIds).isEmpty();
    }

    @Test
    public void testFilterFreeRoomsShouldReturnListOfRoomIdsWhereExistingReservationDoNotConflict(){
//        This reservation starts and ends before observed reservation
        List<ReservationModel> reservationModels1 = List.of(ReservationModel.builder().startDate(OBSERVED_START_DATE_MINUS_10).endDate(OBSERVED_START_DATE_MINUS_9).build());
//        This reservation starts and ends after observed reservation
        List<ReservationModel> reservationModels2 = List.of(ReservationModel.builder().startDate(OBSERVED_END_DATE_PLUS_9).endDate(OBSERVED_END_DATE_PLUS_10).build());

        when(reservationRepositoryService.getReservationsByRoomId(ROOM_ID_1)).thenReturn(reservationModels1);
        when(reservationRepositoryService.getReservationsByRoomId(ROOM_ID_2)).thenReturn(reservationModels2);

        List<Long> resultRoomIds = reservationService.filterFreeRooms(List.of(ROOM_ID_1, ROOM_ID_2), OBSERVED_START_DATE, OBSERVED_END_DATE);

        verify(reservationRepositoryService).getReservationsByRoomId(ROOM_ID_1);
        verify(reservationRepositoryService).getReservationsByRoomId(ROOM_ID_2);
        Assertions.assertThat(resultRoomIds).isNotNull();
        Assertions.assertThat(resultRoomIds).isNotEmpty();
        Assertions.assertThat(resultRoomIds).isEqualTo(List.of(ROOM_ID_1, ROOM_ID_2));
    }

    @Test
    public void testFilterFreeRoomsShouldReturnEmptyListIfObservedRoomHasNonConflictingAndConflictingReservations(){
        List<ReservationModel> reservationModels1 = List.of(
//        This reservation starts and ends before observed reservation (non-conflicting)
                ReservationModel.builder().startDate(OBSERVED_START_DATE_MINUS_10).endDate(OBSERVED_START_DATE_MINUS_9).build(),
//        This reservation starts and ends during the observed reservation (conflicting)
                ReservationModel.builder().startDate(OBSERVED_START_DATE_PLUS_ONE).endDate(OBSERVED_END_DATE_MINUS_ONE).build());

        when(reservationRepositoryService.getReservationsByRoomId(ROOM_ID_1)).thenReturn(reservationModels1);

        List<Long> resultRoomIds = reservationService.filterFreeRooms(List.of(ROOM_ID_1), OBSERVED_START_DATE, OBSERVED_END_DATE);

        verify(reservationRepositoryService).getReservationsByRoomId(ROOM_ID_1);
        Assertions.assertThat(resultRoomIds).isNotNull();
        Assertions.assertThat(resultRoomIds).isEmpty();
    }

    @Test
    public void testFilterFreeRoomsShouldOnlyReturnRoomsWitNonConflictingReservations(){
//        This reservation starts and ends before observed reservation (non-conflicting)
        List<ReservationModel> reservationModels1 = List.of(ReservationModel.builder().startDate(OBSERVED_START_DATE_MINUS_10).endDate(OBSERVED_START_DATE_MINUS_9).build());
//        This reservation starts and ends during the observed reservation (conflicting)
        List<ReservationModel> reservationModels2 = List.of(ReservationModel.builder().startDate(OBSERVED_START_DATE_PLUS_ONE).endDate(OBSERVED_END_DATE_MINUS_ONE).build());

        when(reservationRepositoryService.getReservationsByRoomId(ROOM_ID_1)).thenReturn(reservationModels1);
        when(reservationRepositoryService.getReservationsByRoomId(ROOM_ID_2)).thenReturn(reservationModels2);

        List<Long> resultRoomIds = reservationService.filterFreeRooms(List.of(ROOM_ID_1, ROOM_ID_2), OBSERVED_START_DATE, OBSERVED_END_DATE);

        verify(reservationRepositoryService).getReservationsByRoomId(ROOM_ID_1);
        verify(reservationRepositoryService).getReservationsByRoomId(ROOM_ID_2);
        Assertions.assertThat(resultRoomIds).isNotNull();
        Assertions.assertThat(resultRoomIds).isNotEmpty();
        Assertions.assertThat(resultRoomIds).isEqualTo(List.of(ROOM_ID_1));
    }
}
