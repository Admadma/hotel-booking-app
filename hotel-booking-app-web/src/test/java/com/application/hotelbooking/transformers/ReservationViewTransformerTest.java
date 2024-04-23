package com.application.hotelbooking.transformers;

import com.application.hotelbooking.models.ReservationModel;
import com.application.hotelbooking.views.ReservationView;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationViewTransformerTest {

    private static final ReservationModel RESERVATION_MODEL = new ReservationModel();
    private static final List<ReservationModel> RESERVATION_MODELS = List.of(RESERVATION_MODEL);
    private static final Class<ReservationView> RESERVATION_VIEW_CLASS = ReservationView.class;
    private static final ReservationView RESERVATION_VIEW = new ReservationView();
    private static final List<ReservationView> RESERVATION_VIEWS = List.of(RESERVATION_VIEW);

    @InjectMocks
    private ReservationViewTransformer reservationViewTransformer;
    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testTransformToReservationViewShouldReturnTransformedReservationModel(){
        when(modelMapper.map(RESERVATION_MODEL, RESERVATION_VIEW_CLASS)).thenReturn(RESERVATION_VIEW);

        ReservationView resultReservationView = reservationViewTransformer.transformToReservationView(RESERVATION_MODEL);

        verify(modelMapper).map(RESERVATION_MODEL, RESERVATION_VIEW_CLASS);
        Assertions.assertThat(resultReservationView).isNotNull();
        Assertions.assertThat(resultReservationView).isEqualTo(RESERVATION_VIEW);
    }

    @Test
    public void testTransformToReservationViewsShouldReturnListOfTransformedReservationModels(){
        when(modelMapper.map(RESERVATION_MODEL, RESERVATION_VIEW_CLASS)).thenReturn(RESERVATION_VIEW);

        List<ReservationView> resultReservationViews = reservationViewTransformer.transformToReservationViews(RESERVATION_MODELS);

        verify(modelMapper).map(RESERVATION_MODEL, RESERVATION_VIEW_CLASS);
        Assertions.assertThat(resultReservationViews).isNotNull();
        Assertions.assertThat(resultReservationViews).isNotEmpty();
        Assertions.assertThat(resultReservationViews).isEqualTo(RESERVATION_VIEWS);
    }
}
