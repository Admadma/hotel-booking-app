package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.dto.ReservableRoomDTO;
import com.application.hotelbooking.exceptions.OutdatedReservationException;
import com.application.hotelbooking.services.repositoryservices.ReservationRepositoryService;
import com.application.hotelbooking.services.repositoryservices.RoomRepositoryService;
import com.application.hotelbooking.services.repositoryservices.UserRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);
    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private ReservationRepositoryService reservationRepositoryService;

    @Autowired
    private RoomRepositoryService roomRepositoryService;

    public List<ReservationModel> getReservationsOfUser(String username){
        return reservationRepositoryService.getReservationsByUser(userRepositoryService.getUserByName(username).get());
    }

    public void cancelReservation(Long reservationId){
        // Future logic for refunding transaction would go here
        reservationRepositoryService.delete(reservationId);
    }

    private boolean isRoomAvailableInTimePeriod(List<ReservationModel> reservations, LocalDate selectedStartDate, LocalDate selectedEndDate){
        for (ReservationModel reservation : reservations) {
            if (!(reservation.getStartDate().plusDays(1).isAfter(selectedEndDate) || reservation.getEndDate().minusDays(1).isBefore(selectedStartDate))) {
                // I check each reservation of this room. If it has a single conflict then I can't reserve this in the selected time period.
                return false;
            }
        }
        return true;
    }

    public List<Long> filterFreeRooms(List<Long> roomIds, LocalDate startDate, LocalDate endDate){
        List<Long> freeRooms = new LinkedList<>();
        List<ReservationModel> reservations;

        for (Long roomId : roomIds) {
            reservations = reservationRepositoryService.getReservationsByRoomId(roomId);
            if (reservations.isEmpty() || isRoomAvailableInTimePeriod(reservations, startDate, endDate)){
                freeRooms.add(roomId);
            }
        }

        return freeRooms;
    }

    public int calculateTotalPrice(LocalDate startDate, LocalDate endDate, int pricePerNight){
        return pricePerNight * endDate.compareTo(startDate);
    }

    public ReservationModel prepareReservation(ReservableRoomDTO reservableRoomDTO, String userName){
        return ReservationModel.builder()
                .room(roomRepositoryService.findRoomByNumberAndHotelName(reservableRoomDTO.getRoomNumber(), reservableRoomDTO.getHotelName()))
                .user(userRepositoryService.getUserByName(userName).get())
                .totalPrice(reservableRoomDTO.getTotalPrice())
                .startDate(reservableRoomDTO.getStartDate())
                .endDate(reservableRoomDTO.getEndDate())
                .build();
    }

    public ReservationModel reserveRoom(ReservationModel reservationModel){
        if (isRoomStillAvailable(reservationModel)){
            // If a room has multiple reservations (10000+) this is a more efficient way of checking since it only checks if the version had changed.
            ReservationModel reservation = reservationRepositoryService.save(reservationModel);
            reservation.getRoom().setVersion(reservation.getRoom().getVersion() + 1);
            roomRepositoryService.updateRoom(reservation.getRoom());
            return reservation;
        } else {
            throw new OutdatedReservationException("This reservation is no longer valid");
        }
    }

    private boolean isRoomStillAvailable(ReservationModel reservationModel) {
        //TODO: This is not the correct way to do it. This way the version will also change when the room gets reserved in a different time period. I will need to check all reservations of the room again to see if its still free
        return reservationModel.getRoom().getVersion() == roomRepositoryService.findRoomByNumberAndHotelName(reservationModel.getRoom().getRoomNumber(), reservationModel.getRoom().getHotel().getHotelName()).getVersion();
    }
}
