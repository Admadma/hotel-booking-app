package com.application.hotelbooking.transformers;

import com.application.hotelbooking.dto.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationPlanTransformerTest {
    private static final ReservationPlanDTO RESERVATION_PLAN_DTO = new ReservationPlanDTO();
    private static final Class<ReservationPlanDTO> RESERVATION_PLAN_DTO_CLASS = ReservationPlanDTO.class;
    private static final ReservationPlanServiceDTO RESERVATION_PLAN_SERVICE_DTO = new ReservationPlanServiceDTO();
    private static final Class<ReservationPlanServiceDTO> RESERVATION_PLAN_SERVICE_DTO_CLASS = ReservationPlanServiceDTO.class;

    @InjectMocks
    private ReservationPlanTransformer reservationPlanTransformer;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testTransformToReservationPlanDTOShouldReturnTransformedReservationPlanServiceDTO(){
        when(modelMapper.map(RESERVATION_PLAN_SERVICE_DTO, RESERVATION_PLAN_DTO_CLASS)).thenReturn(RESERVATION_PLAN_DTO);

        ReservationPlanDTO resultReservationPlanDTO = reservationPlanTransformer.transformToReservationPlanDTO(RESERVATION_PLAN_SERVICE_DTO);

        verify(modelMapper).map(RESERVATION_PLAN_SERVICE_DTO, RESERVATION_PLAN_DTO_CLASS);
        Assertions.assertThat(resultReservationPlanDTO).isEqualTo(RESERVATION_PLAN_DTO);
    }

    @Test
    public void testTransformToReservationPlanServiceDTOShouldReturnTransformedReservationPlanDTO(){
        when(modelMapper.map(RESERVATION_PLAN_DTO, RESERVATION_PLAN_SERVICE_DTO_CLASS)).thenReturn(RESERVATION_PLAN_SERVICE_DTO);

        ReservationPlanServiceDTO resultReservationPlanServiceDTO = reservationPlanTransformer.transformToReservationPlanServiceDTO(RESERVATION_PLAN_DTO);

        verify(modelMapper).map(RESERVATION_PLAN_DTO, RESERVATION_PLAN_SERVICE_DTO_CLASS);
        Assertions.assertThat(resultReservationPlanServiceDTO).isEqualTo(RESERVATION_PLAN_SERVICE_DTO);
    }
}
