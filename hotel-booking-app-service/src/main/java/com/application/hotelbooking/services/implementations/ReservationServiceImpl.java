package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.domain.ReservationStatus;
import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.dto.*;
import com.application.hotelbooking.exceptions.OutdatedReservationException;
import com.application.hotelbooking.services.AvailableRoomsFilterService;
import com.application.hotelbooking.services.ReservationConfirmationEmailService;
import com.application.hotelbooking.services.ReservationService;
import com.application.hotelbooking.services.repositoryservices.ReservationRepositoryService;
import com.application.hotelbooking.services.repositoryservices.RoomRepositoryService;
import com.application.hotelbooking.services.repositoryservices.UserRepositoryService;
import com.application.hotelbooking.wrappers.UUIDWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);
    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private ReservationRepositoryService reservationRepositoryService;

    @Autowired
    private AvailableRoomsFilterService availableRoomsFilterService;

    @Autowired
    private RoomRepositoryService roomRepositoryService;

    @Autowired
    private ReservationConfirmationEmailService reservationConfirmationEmailService;

    @Autowired
    private UUIDWrapper uuidWrapper;

    public List<ReservationModel> getReservationsOfUser(String username){
        return reservationRepositoryService.getReservationsByUserId(userRepositoryService.getUserByName(username).get().getId());
    }

    public void cancelReservation(Long reservationId){
        // Future logic for refunding transaction would go here
        reservationRepositoryService.delete(reservationId);
    }

    public int calculateTotalPrice(LocalDate startDate, LocalDate endDate, int pricePerNight){
        return pricePerNight * (int) ChronoUnit.DAYS.between(startDate, endDate);
    }


    public ReservationModel prepareReservation(ReservationPlanServiceDTO reservationPlanServiceDTO, RoomModel roomModel, String userName){
        return ReservationModel.builder()
                .uuid(uuidWrapper.getRandomUUID())
                .room(roomModel)
                .user(userRepositoryService.getUserByName(userName).get())
                .totalPrice(reservationPlanServiceDTO.getTotalPrice())
                .startDate(reservationPlanServiceDTO.getStartDate())
                .endDate(reservationPlanServiceDTO.getEndDate())
                .reservationStatus(ReservationStatus.PLANNED)
                .build();
    }

    public ReservationPlanServiceDTO createReservationPlan(int roomNumber, String hotelName, List<HotelWithReservableRoomsServiceDTO> hotelWithReservableRoomsServiceDTOS){
        HotelWithReservableRoomsServiceDTO hotel = hotelWithReservableRoomsServiceDTOS.stream().filter(hotelWithReservableRoomsServiceDTO -> hotelWithReservableRoomsServiceDTO.getHotelName().equals(hotelName)).findFirst().get();
        UniqueReservableRoomOfHotelServiceDTO room = hotel.getUniqueReservableRoomOfHotelServiceDTOList().stream().filter(uniqueReservableRoomOfHotelServiceDTO -> uniqueReservableRoomOfHotelServiceDTO.getNumber() == roomNumber).findFirst().get();

        return ReservationPlanServiceDTO.builder()
                .hotelName(hotel.getHotelName())
                .city(hotel.getCity())
                .roomType(room.getRoomType())
                .singleBeds(room.getSingleBeds())
                .doubleBeds(room.getDoubleBeds())
                .startDate(room.getStartDate())
                .endDate(room.getEndDate())
                .pricePerNight(room.getPricePerNight())
                .totalPrice(room.getTotalPrice())
                .build();
    }

    public ReservationModel reserveRoom(ReservationPlanServiceDTO reservationPlanServiceDTO, String userName) {
        List<Long> roomIds = roomRepositoryService.getRoomsWithConditions(reservationPlanServiceDTO.getSingleBeds(),
                 reservationPlanServiceDTO.getDoubleBeds(),
                 reservationPlanServiceDTO.getRoomType(),
                 reservationPlanServiceDTO.getHotelName(),
                 reservationPlanServiceDTO.getCity());

        List<Long> availableRoomsIds = availableRoomsFilterService.filterFreeRooms(roomIds, reservationPlanServiceDTO.getStartDate(), reservationPlanServiceDTO.getEndDate());
        for (Long availableRoomsId : availableRoomsIds) {
            RoomModel roomModel = roomRepositoryService.getRoomById(availableRoomsId).get();
            if (roomModel.getPricePerNight() == reservationPlanServiceDTO.getPricePerNight()) {
                ReservationModel reservation = reservationRepositoryService.save(prepareReservation(reservationPlanServiceDTO, roomModel, userName));
                roomRepositoryService.incrementRoomVersion(reservation.getRoom());
                reservationConfirmationEmailService.sendReservationConfirmationEmail(reservation);
                return reservation;
            }
        }
        throw new OutdatedReservationException("No more available rooms found of this type");
    }
}
