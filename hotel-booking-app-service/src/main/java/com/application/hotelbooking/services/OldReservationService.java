package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.exceptions.*;
import com.application.hotelbooking.repositories.ReservationRepository;
import com.application.hotelbooking.transformers.ReservationTransformer;
import com.application.hotelbooking.transformers.RoomTransformer;
import com.application.hotelbooking.transformers.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
public class OldReservationService {

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

    /**
     * It is possible that after entering time period and finding a room, the user doesn't immediately reserve it.
     * In the meantime someone else might take it, so I need to check if the room had any new reservations since then.
     * If a reservation was made on a room, the room's version will increase.
     *
     * @param reservationModel The reservation that needs to be validated and saved
     * @return The reserved room
     */
    public ReservationModel reserve(ReservationModel reservationModel){
        return null;
    }

    public ReservationModel prepareReservation(String roomType, String username, LocalDate selectedStartDate, LocalDate selectedEndDate){
        return null;
    }

    private void isTimePeriodValid(LocalDate selectedStartDate, LocalDate selectedEndDate) {
        if (selectedStartDate.isBefore(LocalDate.now(clock)) || selectedStartDate.isAfter(selectedEndDate)) {
            throw new InvalidTimePeriodException();
        }
    }

    private RoomModel findFreeRoom(List<RoomModel> rooms, LocalDate selectedStartDate, LocalDate selectedEndDate) {
        return null;
    }

    private boolean isRoomAvailableInTimePeriod(List<ReservationModel> reservations, LocalDate selectedStartDate, LocalDate selectedEndDate){
        for (ReservationModel reservation : reservations) {
            if (!(reservation.getStartDate().isAfter(selectedEndDate) || reservation.getEndDate().minusDays(1).isBefore(selectedStartDate))) {
                // I check each reservation of this room. If it has a single conflict then I can't reserve this in the selected time period.
                return false;
            }
        }
        return true;
    }

    private int getTotalPrice(RoomModel roomModel, LocalDate startDate, LocalDate endDate){
        return roomModel.getPricePerNight() * endDate.compareTo(startDate);
    }

    public ReservationModel getReservationById(Long id){
        return null;
    }
    public void deleteReservationOfUser(String username, Long reservationId){

    }

    private boolean reservationNotExists(Long reservationId){
        return reservationRepository.findById(reservationId).isEmpty();
    }

    public List<ReservationModel> getReservationsOfUser(String username){
        return null;
    }

    public void clearReservations(){
        reservationRepository.deleteAll();
    }
}
