package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("familyRoomService")
    private FamilyRoomService familyRoomService;
    @Autowired
    @Qualifier("singleRoomService")
    private SingleRoomService singleRoomService;

    @Autowired
    private UserService userService;

    public Reservation createReservation(Long roomId){
        Reservation reservation = new Reservation(
//                roomService.getRoom(roomId),
                familyRoomService.getRoom(roomId),
                userService.getUserByName("First User"),
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );

        return reservationRepository.save(reservation);
    }

    public Reservation createReservationOnGivenRoomType(String roomType){
        Reservation reservation;
        switch (roomType){
            case "familyRoom":
                reservation = new Reservation(
                        // TODO: later needs logic for checking start-end date, and whether there is a room available
                        familyRoomService.getRooms().get(0),
                        userService.getUserByName("First User"),
                        LocalDate.now(),
                        LocalDate.now().plusDays(7)
                );
                return reservationRepository.save(reservation);
            case "singleRoom":
                reservation = new Reservation(
                        singleRoomService.getRooms().get(0),
                        userService.getUserByName("First User"),
                        LocalDate.now(),
                        LocalDate.now().plusDays(7)
                );
                return reservationRepository.save(reservation);
            default:
                throw new InvalidRoomTypeException();
        }
    }

    public long countRoomsOfGivenType(String roomType){

        switch (roomType){
            case "familyRoom":
                return familyRoomService.getRooms().stream().count();
            case "singleRoom":
                return singleRoomService.getRooms().stream().count();
            default:
                throw new InvalidRoomTypeException();
        }


    }
}
