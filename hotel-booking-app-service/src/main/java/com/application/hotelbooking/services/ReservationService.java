package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.dto.ReservableRoomDTO;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    List<ReservationModel> getReservationsOfUser(String username);
    void cancelReservation(Long reservationId);
    List<Long> filterFreeRooms(List<Long> roomIds, LocalDate startDate, LocalDate endDate);
    int calculateTotalPrice(LocalDate startDate, LocalDate endDate, int pricePerNight);
    ReservationModel prepareReservation(ReservableRoomDTO reservableRoomDTO, String userName);
    @Transactional
    ReservationModel reserveRoom(ReservationModel reservationModel);
}
