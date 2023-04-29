package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.exceptions.*;
import com.application.hotelbooking.repositories.ReservationRepository;
import com.application.hotelbooking.transformers.ReservationTransformer;
import com.application.hotelbooking.transformers.RoomTransformer;
import com.application.hotelbooking.transformers.UserTransformer;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
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

    @Autowired
    private Clock clock;

    public ReservationModel reserve(ReservationModel reservationModel){
        // It is possible that after entering time period and finding a room, the user doesn't immediately reserve it.
        // In the meantime someone else might take it, so I need to check if the room had any new reservations since then (new reservations => the version of the room increased).
        if (!roomService.roomVersionMatches(reservationModel.getRoom())){
            throw new OptimisticLockException();
        }
        ReservationModel reservation = reservationTransformer.transformToReservationModel(reservationRepository.save(reservationTransformer.transformToReservation(reservationModel)));
        roomService.updateRoomVersion(reservationModel.getRoom());
        return reservation;
    }

    public ReservationModel prepareReservation(String roomType, String username, LocalDate selectedStartDate, LocalDate selectedEndDate){
        isTimePeriodValid(selectedStartDate, selectedEndDate);

        RoomModel room = findFreeRoom(roomService.findAllRoomsOfGivenType(roomType), selectedStartDate, selectedEndDate);

        return ReservationModel.builder()
                .room(room)
                .user(userService.getUsersByName(username).get(0))
                .startDate(selectedStartDate)
                .endDate(selectedEndDate)
                .build();
    }

    private void isTimePeriodValid(LocalDate selectedStartDate, LocalDate selectedEndDate) {
        if (selectedStartDate.isBefore(LocalDate.now(clock)) || selectedStartDate.isAfter(selectedEndDate)) {
            throw new InvalidTimePeriodException();
        }
    }

    private RoomModel findFreeRoom(List<RoomModel> rooms, LocalDate selectedStartDate, LocalDate selectedEndDate) {
        List<ReservationModel> reservations;
        for (RoomModel room: rooms) {
            reservations = reservationTransformer.transformToReservationModels(reservationRepository.findAllByRoom(roomTransformer.transformToRoom(room)));
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

    public ReservationModel getReservationById(Long id){
        return reservationTransformer.transformToReservationModel(reservationRepository.findById(id).get());
    }
    public void deleteReservationOfUser(String username, Long reservationId){
        if (reservationNotExists(reservationId)){
            throw new CancellationErrorException("Could nto find reservation with id:" + reservationId);
        }
        ReservationModel reservation = getReservationById(reservationId);
        if (!reservation.getUser().getUsername().equals(username)){
            throw new CancellationErrorException("The selected reservation doesn't belong to the current user");
        }
        reservationRepository.delete(reservationTransformer.transformToReservation(reservation));
    }

    private boolean reservationNotExists(Long reservationId){
        return reservationRepository.findById(reservationId).isEmpty();
    }

    public List<ReservationModel> getReservationsOfUser(String username){
        return reservationTransformer.transformToReservationModels(reservationRepository.findAllByUser(userTransformer.transformToUser(userService.getUsersByName(username).get(0))));
    }

    public void clearReservations(){
        reservationRepository.deleteAll();
    }
}
