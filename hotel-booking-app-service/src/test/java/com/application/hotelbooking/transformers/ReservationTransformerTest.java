package com.application.hotelbooking.transformers;

import com.application.hotelbooking.entities.Reservation;
import com.application.hotelbooking.models.ReservationModel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationTransformerTest {
    public static final Class<Reservation> RESERVATION_CLASS = Reservation.class;
    public static final Reservation RESERVATION = new Reservation();
    public static final List<Reservation> RESERVATIONS = List.of(RESERVATION);
    public static final Optional<Reservation> OPTIONAL_RESERVATION = Optional.of(RESERVATION);
    public static final Optional<Reservation> EMPTY_RESERVATION = Optional.empty();
    public static final Class<ReservationModel> RESERVATION_MODEL_CLASS = ReservationModel.class;
    public static final ReservationModel RESERVATION_MODEL = new ReservationModel();
    public static final List<ReservationModel> RESERVATION_MODELS = List.of(RESERVATION_MODEL);
    public static final Optional<ReservationModel> OPTIONAL_RESERVATION_MODEL = Optional.of(RESERVATION_MODEL);


    @InjectMocks
    private ReservationTransformer reservationTransformer;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testTransformToReservationShouldReturnTransformedReservationModel(){
        when(modelMapper.map(RESERVATION_MODEL, RESERVATION_CLASS)).thenReturn(RESERVATION);

        Reservation resultReservation = reservationTransformer.transformToReservation(RESERVATION_MODEL);

        verify(modelMapper).map(RESERVATION_MODEL, RESERVATION_CLASS);
        Assertions.assertThat(resultReservation).isNotNull();
        Assertions.assertThat(resultReservation).isEqualTo(RESERVATION);
    }

    @Test
    public void testTransformToReservationModelShouldReturnTransformedReservation(){
        when(modelMapper.map(RESERVATION, RESERVATION_MODEL_CLASS)).thenReturn(RESERVATION_MODEL);

        ReservationModel resultReservationModel = reservationTransformer.transformToReservationModel(RESERVATION);

        verify(modelMapper).map(RESERVATION, RESERVATION_MODEL_CLASS);
        Assertions.assertThat(resultReservationModel).isNotNull();
        Assertions.assertThat(resultReservationModel).isEqualTo(RESERVATION_MODEL);
    }

    @Test
    public void testTransformToOptionalReservationModelShouldReturnOptionalOfReservationModel(){
        when(modelMapper.map(OPTIONAL_RESERVATION, RESERVATION_MODEL_CLASS)).thenReturn(RESERVATION_MODEL);

        Optional<ReservationModel> resultOptionalReservationModel = reservationTransformer.transformToOptionalReservationModel(OPTIONAL_RESERVATION);

        verify(modelMapper).map(OPTIONAL_RESERVATION, RESERVATION_MODEL_CLASS);
        Assertions.assertThat(resultOptionalReservationModel).isNotNull();
        Assertions.assertThat(resultOptionalReservationModel).isNotEmpty();
        Assertions.assertThat(resultOptionalReservationModel).isEqualTo(OPTIONAL_RESERVATION_MODEL);
    }

    @Test
    public void testTransformToOptionalReservationModelShouldReturnEmptyOptionalOfReservationModel(){

        Optional<ReservationModel> resultOptionalReservationModel = reservationTransformer.transformToOptionalReservationModel(EMPTY_RESERVATION);

        Assertions.assertThat(resultOptionalReservationModel).isNotNull();
        Assertions.assertThat(resultOptionalReservationModel).isEmpty();
    }

    @Test
    public void testTransformToReservationModelsShouldReturnListOfTransformedReservations(){
        when(modelMapper.map(RESERVATION, RESERVATION_MODEL_CLASS)).thenReturn(RESERVATION_MODEL);

        List<ReservationModel> resultReservationModels = reservationTransformer.transformToReservationModels(RESERVATIONS);

        verify(modelMapper).map(RESERVATION, RESERVATION_MODEL_CLASS);
        Assertions.assertThat(resultReservationModels).isNotNull();
        Assertions.assertThat(resultReservationModels).isNotEmpty();
        Assertions.assertThat(resultReservationModels).isEqualTo(RESERVATION_MODELS);
    }
}
