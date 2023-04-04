package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    public Reservation createReservation(Long roomId){
        Reservation reservation = new Reservation(
                roomService.getRoom(roomId),
                userService.getUserByName("First User"),
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );

        return reservationRepository.save(reservation);
    }
}
