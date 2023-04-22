package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.exceptions.InvalidUserException;
import com.application.hotelbooking.repositories.ReservationRepository;
import com.application.hotelbooking.exceptions.InvalidTimePeriodException;
import com.application.hotelbooking.exceptions.NoRoomsAvailableException;
import com.application.hotelbooking.transformers.ReservationTransformer;
import com.application.hotelbooking.transformers.RoomTransformer;
import com.application.hotelbooking.transformers.UserTransformer;
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

    @Autowired
    private UserTransformer userTransformer;

    @Autowired
    private ReservationTransformer reservationTransformer;

    @Autowired
    private RoomTransformer roomTransformer;

    public ReservationModel reserve(String roomType, String username, LocalDate selectedStartDate, LocalDate selectedEndDate){
        // TODO selecting room type and time period might be entered separately in the future. Maybe not, if clicking on a room type first navigates to info page, and this method is only called once something (like time period) on that page is selected
        if (!userService.userExists(username)){
            throw new InvalidUserException("Could not find this exact user in the database: " + username);
        }

        if (selectedStartDate.isAfter(selectedEndDate)) {
            throw new InvalidTimePeriodException();
        }

        List<RoomModel> rooms = roomService.findAllRoomsOfGivenType(roomType);

        RoomModel room = findFreeRoom(rooms, selectedStartDate, selectedEndDate);

        Reservation reservation = new Reservation(
                roomTransformer.transformToRoom(room),
                userTransformer.transformToUser(userService.getUserByName(username).get(0)),
                selectedStartDate,
                selectedEndDate
        );

        return reservationTransformer.transformToReservationModel(reservationRepository.save(reservation));
    }

    public ReservationModel reserve(ReservationModel reservationModel, String username){
        // TODO selecting room type and time period might be entered separately in the future. Maybe not, if clicking on a room type first navigates to info page, and this method is only called once something (like time period) on that page is selected
        if (!userService.userExists(username)){
            throw new InvalidUserException("Could not find this exact user in the database: " + username);
        }

        if (reservationModel.getStartDate().isAfter(reservationModel.getEndDate())) {
            throw new InvalidTimePeriodException();
        }

        List<RoomModel> rooms = roomService.findAllRoomsOfGivenType(reservationModel.getRoom().getRoomType());

        RoomModel room = findFreeRoom(rooms, reservationModel.getStartDate(), reservationModel.getEndDate());

        Reservation reservation = new Reservation(
                roomTransformer.transformToRoom(room),
                userTransformer.transformToUser(userService.getUserByName(username).get(0)),
                reservationModel.getStartDate(),
                reservationModel.getEndDate()
        );

        return reservationTransformer.transformToReservationModel(reservationRepository.save(reservation));
    }

    private RoomModel findFreeRoom(List<RoomModel> rooms, LocalDate selectedStartDate, LocalDate selectedEndDate) {
        List<ReservationModel> reservations;
        for (RoomModel room: rooms) {
            reservations = reservationTransformer.transformToReservationModels(reservationRepository.getReservationsOfRoom(room.getId()));
            if (reservations.isEmpty()){
                // if the room has no reservations associated with it, that means we can simply reserve it
                return room;
            }
            if (isRoomAvailableInTimePeriod(reservations, selectedStartDate, selectedEndDate)){
                // if there are reservations associated with the room then we need to check if any of them conflicts with the selected time period
                return room;
            }
        }
        throw new NoRoomsAvailableException();
    }

    private boolean isRoomAvailableInTimePeriod(List<ReservationModel> reservations, LocalDate selectedStartDate, LocalDate selectedEndDate){
        for (ReservationModel reservation : reservations) {
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
