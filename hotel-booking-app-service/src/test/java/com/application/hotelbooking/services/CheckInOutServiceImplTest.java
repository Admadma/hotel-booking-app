package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.domain.ReservationStatus;
import com.application.hotelbooking.services.implementations.CheckInOutServiceImpl;
import com.application.hotelbooking.services.repositoryservices.ReservationRepositoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CheckInOutServiceImplTest {

    private static final UUID TEST_UUID = UUID.fromString("2a167ea9-850c-4059-8163-6f941561c419");
    private static final ReservationModel RESERVATION_MODEL_PLANNED = ReservationModel.builder()
            .uuid(TEST_UUID)
            .reservationStatus(ReservationStatus.PLANNED)
            .build();
    private static final ReservationModel RESERVATION_MODEL_ACTIVE = ReservationModel.builder()
            .uuid(TEST_UUID)
            .reservationStatus(ReservationStatus.ACTIVE)
            .build();
    private static final ReservationModel RESERVATION_MODEL_COMPLETED = ReservationModel.builder()
            .uuid(TEST_UUID)
            .reservationStatus(ReservationStatus.COMPLETED)
            .build();
    private static final Optional<ReservationModel> OPTIONAL_RESERVATION_MODEL = Optional.of(RESERVATION_MODEL_PLANNED);

    @InjectMocks
    private CheckInOutServiceImpl checkInOutServiceImpl;

    @Mock
    private ReservationRepositoryService reservationRepositoryService;

    @BeforeEach
    void setUp() {
        RESERVATION_MODEL_PLANNED.setReservationStatus(ReservationStatus.PLANNED);
    }

    @Test
    public void testGetReservationDetailsShouldReturnReservationModelOfReservationWithGivenUUID(){
        when(reservationRepositoryService.getReservationByUuid(TEST_UUID)).thenReturn(OPTIONAL_RESERVATION_MODEL);

        ReservationModel resultReservationModel = checkInOutServiceImpl.getReservationDetails(TEST_UUID);

        verify(reservationRepositoryService).getReservationByUuid(TEST_UUID);
        Assertions.assertThat(resultReservationModel.getUuid()).isEqualTo(TEST_UUID);
    }

    @Test
    public void testCheckInGuestShouldSetReservationStatusToACTIVEAndReturnSavedReservation(){
        when(reservationRepositoryService.getReservationByUuid(TEST_UUID)).thenReturn(OPTIONAL_RESERVATION_MODEL);
        when(reservationRepositoryService.save(RESERVATION_MODEL_ACTIVE)).thenReturn(RESERVATION_MODEL_ACTIVE);

        ReservationModel resultReservationModel = checkInOutServiceImpl.checkInGuest(TEST_UUID);

        verify(reservationRepositoryService).getReservationByUuid(TEST_UUID);
        verify(reservationRepositoryService).save(RESERVATION_MODEL_ACTIVE);
        Assertions.assertThat(resultReservationModel.getUuid()).isEqualTo(TEST_UUID);
        Assertions.assertThat(resultReservationModel.getReservationStatus()).isEqualTo(ReservationStatus.ACTIVE);
    }

    @Test
    public void testCheckOutGuestShouldSetReservationStatusToCOMPLETEDAndReturnSavedReservation(){
        when(reservationRepositoryService.getReservationByUuid(TEST_UUID)).thenReturn(OPTIONAL_RESERVATION_MODEL);
        when(reservationRepositoryService.save(RESERVATION_MODEL_COMPLETED)).thenReturn(RESERVATION_MODEL_COMPLETED);

        ReservationModel resultReservationModel = checkInOutServiceImpl.checkOutGuest(TEST_UUID);

        verify(reservationRepositoryService).getReservationByUuid(TEST_UUID);
        verify(reservationRepositoryService).save(RESERVATION_MODEL_COMPLETED);
        Assertions.assertThat(resultReservationModel.getUuid()).isEqualTo(TEST_UUID);
        Assertions.assertThat(resultReservationModel.getReservationStatus()).isEqualTo(ReservationStatus.COMPLETED);
    }
}
