package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.domain.ReservationModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public Reservation transformToReservation(ReservationModel reservationModel){
        return modelMapper.map(reservationModel, Reservation.class);
    }

    public ReservationModel transformToReservationModel(Reservation reservation){
        return modelMapper.map(reservation, ReservationModel.class);
    }

    public List<ReservationModel> transformToReservationModels(List<Reservation> reservations){
        return reservations.stream()
                .map(reservation -> modelMapper.map(reservation, ReservationModel.class))
                .collect(Collectors.toList());
    }
}
