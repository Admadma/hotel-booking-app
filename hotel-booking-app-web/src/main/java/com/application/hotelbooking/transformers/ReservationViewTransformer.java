package com.application.hotelbooking.transformers;

import com.application.hotelbooking.models.ReservationModel;
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

    public List<ReservationView> transformToReservationViews(List<ReservationModel> reservationModels){
        return reservationModels.stream()
                .map(reservationModel -> modelMapper.map(reservationModel, ReservationView.class))
                .collect(Collectors.toList());
    }
}
