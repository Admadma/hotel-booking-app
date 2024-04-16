package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.dto.HotelWithReservableRoomsServiceDTO;
import com.application.hotelbooking.dto.ReservationPlanServiceDTO;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    List<ReservationModel> getReservationsOfUser(String username);
    void cancelReservation(Long reservationId);
    List<Long> filterFreeRooms(List<Long> roomIds, LocalDate startDate, LocalDate endDate);
    int calculateTotalPrice(LocalDate startDate, LocalDate endDate, int pricePerNight);
    ReservationModel prepareReservation(ReservationPlanServiceDTO reservationPlanServiceDTO, RoomModel roomModel, String userName)
    ReservationPlanServiceDTO createReservationPlan(int roomNumber, String hotelName, List<HotelWithReservableRoomsServiceDTO> hotelWithReservableRoomsServiceDTOS);
    @Transactional
    ReservationModel reserveRoom(ReservationPlanServiceDTO reservationPlanServiceDTO, String userName);
}
