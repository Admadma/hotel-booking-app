package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.*;
import com.application.hotelbooking.services.implementations.AvailableRoomsFilterServiceImpl;
import com.application.hotelbooking.services.repositoryservices.ReservationRepositoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvailableRoomsFilterServiceImplTest {

    public static final long ROOM_ID_1 = 1l;
    public static final long ROOM_ID_2 = 2l;
    public static final long ROOM_ID_3 = 3l;
    public static final long ROOM_ID_4 = 4l;
    public static final LocalDate OBSERVED_START_DATE = LocalDate.of(2024, 3, 1);
    public static final LocalDate OBSERVED_END_DATE = LocalDate.of(2024, 3, 5);
    public static final LocalDate OBSERVED_START_DATE_PLUS_ONE = OBSERVED_START_DATE.plusDays(1);
    public static final LocalDate OBSERVED_END_DATE_MINUS_ONE = OBSERVED_END_DATE.minusDays(1);
    public static final LocalDate OBSERVED_START_DATE_MINUS_9 = OBSERVED_START_DATE.minusDays(9);
    public static final LocalDate OBSERVED_START_DATE_MINUS_10 = OBSERVED_START_DATE.minusDays(10);
    public static final LocalDate OBSERVED_END_DATE_PLUS_9 = OBSERVED_END_DATE.plusDays(9);
    public static final LocalDate OBSERVED_END_DATE_PLUS_10 = OBSERVED_END_DATE.plusDays(10);

    @InjectMocks
    private AvailableRoomsFilterServiceImpl availableRoomsFilterServiceImpl;

    @Mock
    private ReservationRepositoryService reservationRepositoryService;

    @Test
    public void testFilterFreeRoomsShouldAllowRoomsWithNoReservations(){
        when(reservationRepositoryService.getReservationsByRoomId(ROOM_ID_1)).thenReturn(List.of());

        List<Long> resultRoomIds = availableRoomsFilterServiceImpl.filterFreeRooms(List.of(ROOM_ID_1), OBSERVED_START_DATE, OBSERVED_END_DATE);

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

        List<Long> resultRoomIds = availableRoomsFilterServiceImpl.filterFreeRooms(List.of(ROOM_ID_1, ROOM_ID_2, ROOM_ID_3, ROOM_ID_4), OBSERVED_START_DATE, OBSERVED_END_DATE);

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

        List<Long> resultRoomIds = availableRoomsFilterServiceImpl.filterFreeRooms(List.of(ROOM_ID_1, ROOM_ID_2), OBSERVED_START_DATE, OBSERVED_END_DATE);

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

        List<Long> resultRoomIds = availableRoomsFilterServiceImpl.filterFreeRooms(List.of(ROOM_ID_1), OBSERVED_START_DATE, OBSERVED_END_DATE);

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

        List<Long> resultRoomIds = availableRoomsFilterServiceImpl.filterFreeRooms(List.of(ROOM_ID_1, ROOM_ID_2), OBSERVED_START_DATE, OBSERVED_END_DATE);

        verify(reservationRepositoryService).getReservationsByRoomId(ROOM_ID_1);
        verify(reservationRepositoryService).getReservationsByRoomId(ROOM_ID_2);
        Assertions.assertThat(resultRoomIds).isNotNull();
        Assertions.assertThat(resultRoomIds).isNotEmpty();
        Assertions.assertThat(resultRoomIds).isEqualTo(List.of(ROOM_ID_1));
    }
}
