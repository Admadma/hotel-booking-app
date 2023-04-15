package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.repositories.ReservationRepository;
import com.application.hotelbooking.exceptions.InvalidTimePeriodException;
import com.application.hotelbooking.exceptions.NoRoomsAvailableException;
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
    private UserService userService;

    public Reservation reserve(String roomType, LocalDate selectedStartDate, LocalDate selectedEndDate){
        // TODO selecting room type and time period might be entered separately in the future. Maybe not, if clicking on a room type first navigates to info page, and this method is only called once something (like time period) on that page is selected
        if (selectedStartDate.isAfter(selectedEndDate)) {
            throw new InvalidTimePeriodException();
        }

        List<Room> rooms = roomService.findAllRoomsOfGivenType(roomType);

        Room room = findFreeRoom(rooms, selectedStartDate, selectedEndDate);

        Reservation reservation = new Reservation(
                room,
                userService.getUserByName("First User").get(0),
                selectedStartDate,
                selectedEndDate
        );

        return reservationRepository.save(reservation);
    }

    private Room findFreeRoom(List<Room> rooms, LocalDate selectedStartDate, LocalDate selectedEndDate) {
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
        throw new NoRoomsAvailableException();
    }

    private boolean isRoomAvailableInTimePeriod(List<Reservation> reservations, LocalDate selectedStartDate, LocalDate selectedEndDate){
        for (Reservation reservation : reservations) {
            if (!(reservation.getStartDate().isAfter(selectedEndDate) || reservation.getEndDate().minusDays(1).isBefore(selectedStartDate))) {
                return false;
            }
        }
        return true;
    }
    public List<Reservation> getReservations(){
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsOfRoom(Long room_id){
        return reservationRepository.getReservationsOfRoom(room_id);
    }

    public void clearReservations(){
        reservationRepository.deleteAll();
    }
}
