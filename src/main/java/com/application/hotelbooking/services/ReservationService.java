package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
                        // TODO: JOINED inheritance strategy allows the use of a common RoomRepository for all rooms. Now I will only need to decide what room object to create and persist it the same way.
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

    public List<Reservation> getReservations(){
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsOfRoom(Long room_id){
        return reservationRepository.getReservationsOfRoom(room_id, LocalDate.now().minusDays(1));
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
