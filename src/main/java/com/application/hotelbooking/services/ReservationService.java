package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.repositories.ReservationRepository;
import com.application.hotelbooking.services.exceptions.InvalidRoomTypeException;
import com.application.hotelbooking.services.exceptions.InvalidTimePeriodException;
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

    public Reservation reserve(String roomType, LocalDate selectedStartDate, LocalDate selectedEndDate){
        // TODO selecting room type and time period might be entered separately in the future. Maybe not, if clicking on a room type first navigates to info page, and this method is only called once something (like time period) on that page is selected
        // get list of {type} rooms (all of them, not just from the reservations)
        // for each room check if it is present in the reservations table
        //      not present:    the room has no reservations associated with it -> it can be reserved
        //      present:        check start-end date conflicts:
        //                          if there is no reservation with conflicting time -> it can be reserved
        //                          if there is a conflict, this room in this time period can not be reserved. Go to next iteration
        // logic:
        //      first check all rooms if there is one that is not present   (danger of running through large number of rooms even if there would be no time conflict )
        //      check each room whether its present and if it is then check for time conflicts (danger of running through large number of reservations, each with time comparisons)

        // plan: first check each room
        // then if all of them has reservation associated then:
        //  start checking from the start but now the time conflict


//        switch (roomType){
//            case "familyRoom":
//                // get a list of familyRooms
//        }
//        roomService

        if (selectedStartDate.isAfter(selectedEndDate)) {
            throw new InvalidTimePeriodException();
        }

        List<Room> rooms = roomService.findAllRoomsOfGivenType(roomType);

        Room room = findFreeRoom(rooms, selectedStartDate, selectedEndDate);

        Reservation reservation = new Reservation(
                room,
                userService.getUserByName("First User"),
                selectedStartDate,
                selectedEndDate
        );

        return reservationRepository.save(reservation);
    }

    private Room findFreeRoom(List<Room> rooms, LocalDate selectedStartDate, LocalDate selectedEndDate){
        List<Reservation> reservations;
        for (Room room: rooms) {
            reservations = reservationRepository.getReservationsOfRoom(room.getId());
            if (reservations.isEmpty()){
                // if the room has no reservations associated with it, that means we can simply reserve it
                return room;
            }
            if (isRoomAvailableInTimePeriod(reservations, selectedStartDate, selectedEndDate)){
                // if there are reservations associated with the room then we need to check if any of them conflicts with the selected time epriod
                return room;
            }
        }
        // TODO: handle case if there is no available room of given type and time period
        return null;
    }

    private boolean isRoomAvailableInTimePeriod(List<Reservation> reservations, LocalDate selectedStartDate, LocalDate selectedEndDate){
        for (Reservation reservation : reservations) {
            // A1---A2  B1---B2
            // B1 > A2
            // or
            // B1---B2 A1---A2
            // B2 < A1
            // (B1 > A2 or B2 < A1) == true means that the time periods do not overlap. If it is false then they do overlap
            if (!(reservation.getStartDate().isAfter(selectedEndDate) || reservation.getEndDate().isBefore(selectedStartDate))) {
                return false;
            }
        }
        return true;
    }


    public Reservation createReservationOnGivenRoomType(String roomType){
        Reservation reservation;
        switch (roomType){
            case "familyRoom":
                reservation = new Reservation(
                        // TODO: later needs logic for checking start-end date, and whether there is a room available
                        // TODO: JOINED inheritance strategy allows the use of a common RoomRepository for all rooms. Now I will only need to decide what room object to create and persist it the same way.
//                        familyRoomService.getRooms().get(0),
                        roomService.findAllRoomsOfGivenType(roomType).get(0),
                        userService.getUserByName("First User"),
                        LocalDate.now(),
                        LocalDate.now().plusDays(7)
                );
                System.out.println("trying to insert");
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
        return reservationRepository.getReservationsOfRoom(room_id);
    }

    public long countRoomsOfGivenType(String roomType){

        System.out.println("getFamilyRooms: "+familyRoomService.getRooms().toString());

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
