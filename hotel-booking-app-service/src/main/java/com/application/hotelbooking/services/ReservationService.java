package com.application.hotelbooking.services;

import com.application.hotelbooking.models.ReservationModel;
import com.application.hotelbooking.models.RoomModel;
import com.application.hotelbooking.dto.HotelWithReservableRoomsServiceDTO;
import com.application.hotelbooking.dto.ReservationPlanServiceDTO;
import com.application.hotelbooking.exceptions.InvalidReservationException;
import com.application.hotelbooking.exceptions.InvalidTokenException;
import com.application.hotelbooking.exceptions.InvalidUserException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReservationService {
    List<ReservationModel> getReservationsOfUser(String username);
    void cancelReservation(UUID uuid, String userName) throws InvalidTokenException, InvalidReservationException, InvalidUserException;
    int calculateTotalPrice(LocalDate startDate, LocalDate endDate, int pricePerNight);
    ReservationPlanServiceDTO createReservationPlan(int roomNumber, String hotelName, List<HotelWithReservableRoomsServiceDTO> hotelWithReservableRoomsServiceDTOS);
    ReservationModel prepareReservation(ReservationPlanServiceDTO reservationPlanServiceDTO, RoomModel roomModel, String userName);
    @Transactional
    ReservationModel reserveRoom(ReservationPlanServiceDTO reservationPlanServiceDTO, String userName);
}
