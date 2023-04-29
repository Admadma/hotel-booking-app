package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.domain.ReservationView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationViewTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public ReservationView transformToReservationView(ReservationModel reservationModel){
        return modelMapper.map(reservationModel, ReservationView.class);
    }

    public ReservationModel transformToReservationModel(ReservationView reservationView){
        return modelMapper.map(reservationView, ReservationModel.class);
    }

    public List<ReservationView> transformToReservationViews(List<ReservationModel> reservations){
        return reservations.stream()
                .map(reservation -> modelMapper.map(reservation, ReservationView.class))
                .collect(Collectors.toList());
    }
}
