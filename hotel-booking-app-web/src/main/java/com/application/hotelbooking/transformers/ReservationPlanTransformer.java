package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.domain.ReservationView;
import com.application.hotelbooking.dto.ReservationPlanDTO;
import com.application.hotelbooking.dto.ReservationPlanServiceDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationPlanTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public ReservationPlanDTO transformToReservationPlanDTO(ReservationPlanServiceDTO reservationPlanServiceDTO){
        return modelMapper.map(reservationPlanServiceDTO, ReservationPlanDTO.class);
    }

    public ReservationPlanServiceDTO transformToReservationPlanServiceDTO(ReservationPlanDTO reservationPlanDTO) {
        return modelMapper.map(reservationPlanDTO, ReservationPlanServiceDTO.class);
    }
}
