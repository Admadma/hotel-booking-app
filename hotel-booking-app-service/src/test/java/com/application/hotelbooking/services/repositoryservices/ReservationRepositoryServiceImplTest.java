package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.domain.*;
import com.application.hotelbooking.repositories.ReservationRepository;
import com.application.hotelbooking.services.repositoryservices.implementations.ReservationRepositoryServiceImpl;
import com.application.hotelbooking.transformers.ReservationTransformer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationRepositoryServiceImplTest {

    private static final Long ID = 1l;
    private static final Reservation RESERVATION = Reservation.builder().user(User.builder().id(ID).build()).room(Room.builder().id(ID).build()).build();
    private static final ReservationModel RESERVATION_MODEL = ReservationModel.builder().user(UserModel.builder().id(ID).build()).room(RoomModel.builder().id(ID).build()).build();
    private static final List<Reservation> RESERVATIONS = List.of(RESERVATION);
    private static final List<Reservation> EMPTY_RESERVATIONS = List.of(RESERVATION);
    private static final List<ReservationModel> RESERVATION_MODELS = List.of();
    private static final List<ReservationModel> EMPTY_RESERVATION_MODELS = List.of();
    @InjectMocks
    private ReservationRepositoryServiceImpl reservationRepositoryService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTransformer reservationTransformer;

    @Test
    public void testSaveShouldReturnReservationModelOfSavedReservation() {
        when(reservationTransformer.transformToReservation(RESERVATION_MODEL)).thenReturn(RESERVATION);
        when(reservationRepository.save(RESERVATION)).thenReturn(RESERVATION);
        when(reservationTransformer.transformToReservationModel(RESERVATION)).thenReturn(RESERVATION_MODEL);

        ReservationModel savedReservation = reservationRepositoryService.save(RESERVATION_MODEL);

        verify(reservationTransformer).transformToReservation(RESERVATION_MODEL);
        verify(reservationRepository).save(RESERVATION);
        verify(reservationTransformer).transformToReservationModel(RESERVATION);
        Assertions.assertThat(savedReservation).isNotNull();
        Assertions.assertThat(savedReservation).isEqualTo(RESERVATION_MODEL);
    }

    @Test
    public void testGetReservationsByRoomIdShouldReturnListOfReservationModels() {
        when(reservationRepository.findAllByRoomId(ID)).thenReturn(RESERVATIONS);
        when(reservationTransformer.transformToReservationModels(RESERVATIONS)).thenReturn(RESERVATION_MODELS);

        List<ReservationModel> reservationsResult = reservationRepositoryService.getReservationsByRoomId(ID);

        verify(reservationRepository).findAllByRoomId(ID);
        verify(reservationTransformer).transformToReservationModels(RESERVATIONS);
        Assertions.assertThat(reservationsResult).isNotNull();
        Assertions.assertThat(reservationsResult).isEqualTo(RESERVATION_MODELS);
    }

    @Test
    public void testGetReservationsByRoomIdShouldReturnEmptyListIfRoomIdNotFound() {
        when(reservationRepository.findAllByRoomId(ID)).thenReturn(EMPTY_RESERVATIONS);
        when(reservationTransformer.transformToReservationModels(EMPTY_RESERVATIONS)).thenReturn(EMPTY_RESERVATION_MODELS);

        List<ReservationModel> reservationsResult = reservationRepositoryService.getReservationsByRoomId(ID);

        verify(reservationRepository).findAllByRoomId(ID);
        verify(reservationTransformer).transformToReservationModels(EMPTY_RESERVATIONS);
        Assertions.assertThat(reservationsResult).isNotNull();
        Assertions.assertThat(reservationsResult).isEmpty();
    }

    @Test
    public void testGetReservationsByUserIdShouldReturnListOfReservationModels() {
        when(reservationRepository.findAllByUserId(ID)).thenReturn(RESERVATIONS);
        when(reservationTransformer.transformToReservationModels(RESERVATIONS)).thenReturn(RESERVATION_MODELS);

        List<ReservationModel> reservationsResult = reservationRepositoryService.getReservationsByUserId(ID);

        verify(reservationRepository).findAllByUserId(ID);
        verify(reservationTransformer).transformToReservationModels(RESERVATIONS);
        Assertions.assertThat(reservationsResult).isNotNull();
        Assertions.assertThat(reservationsResult).isEqualTo(RESERVATION_MODELS);
    }

    @Test
    public void testGetReservationsByUserIdShouldReturnEmptyListIfUserIdNotFound() {
        when(reservationRepository.findAllByUserId(ID)).thenReturn(EMPTY_RESERVATIONS);
        when(reservationTransformer.transformToReservationModels(EMPTY_RESERVATIONS)).thenReturn(EMPTY_RESERVATION_MODELS);

        List<ReservationModel> reservationsResult = reservationRepositoryService.getReservationsByUserId(ID);

        verify(reservationRepository).findAllByUserId(ID);
        verify(reservationTransformer).transformToReservationModels(EMPTY_RESERVATIONS);
        Assertions.assertThat(reservationsResult).isNotNull();
        Assertions.assertThat(reservationsResult).isEmpty();
    }

    @Test
    public void testDeleteShouldCallDeleteByIdWithProvidedId() {
        doNothing().when(reservationRepository).deleteById(ID);

        reservationRepositoryService.delete(ID);

        verify(reservationRepository).deleteById(ID);
    }
}
