package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.dto.ReservationDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public ReservationDTO transformToReservationDTO(Reservation reservation){
        return modelMapper.map(reservation, ReservationDTO.class);
    }

    public Reservation transformToReservation(ReservationDTO reservationDTO){
        return modelMapper.map(reservationDTO, Reservation.class);
    }

    public List<ReservationDTO> transformToReservationDTOs(List<Reservation> reservations){
        return reservations.stream()
                .map(reservation -> modelMapper.map(reservation, ReservationDTO.class))
                .collect(Collectors.toList());
    }
}
