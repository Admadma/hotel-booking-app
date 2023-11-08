package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.domain.ReservationView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReservationViewTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public ReservationView transformToReservationView(ReservationModel reservationModel){
        return modelMapper.map(reservationModel, ReservationView.class);
    }

    public ReservationModel transformToReservationModel(ReservationView reservationView) {
        return modelMapper.map(reservationView, ReservationModel.class);
    }
}
