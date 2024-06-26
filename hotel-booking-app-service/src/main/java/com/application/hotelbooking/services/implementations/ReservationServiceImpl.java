package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.models.ReservationModel;
import com.application.hotelbooking.models.ReservationStatus;
import com.application.hotelbooking.models.RoomModel;
import com.application.hotelbooking.dto.*;
import com.application.hotelbooking.exceptions.InvalidReservationException;
import com.application.hotelbooking.exceptions.InvalidTokenException;
import com.application.hotelbooking.exceptions.InvalidUserException;
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
import java.util.Optional;
import java.util.UUID;

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

    public void cancelReservation(UUID uuid, String userName) throws InvalidTokenException, InvalidReservationException, InvalidUserException {
        // Future logic for refunding transaction would go here
        Optional<ReservationModel> reservationModel = reservationRepositoryService.getReservationByUuid(uuid);

        if (reservationModel.isEmpty()) {
            throw new InvalidTokenException("Could not find a reservation with that UUID");
        }

        if (!ReservationStatus.PLANNED.equals(reservationModel.get().getReservationStatus())) {
            throw new InvalidReservationException("User attempted to delete ACTIVE or COMPLETED reservation");
        }

        if (!userName.equals(reservationModel.get().getUser().getUsername())) {
            throw new InvalidUserException("This reservation does not belong to this user");
        }

        reservationRepositoryService.delete(reservationModel.get().getId());
    }

    public int calculateTotalPrice(LocalDate startDate, LocalDate endDate, int pricePerNight){
        return pricePerNight * (int) ChronoUnit.DAYS.between(startDate, endDate);
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

    public ReservationModel prepareReservation(ReservationPlanServiceDTO reservationPlanServiceDTO, RoomModel roomModel, String userName){
        return ReservationModel.builder()
                .uuid(uuidWrapper.getRandomUUID())
                .room(roomModel)
                .user(userRepositoryService.getUserByName(userName).get())
                .totalPrice(calculateTotalPrice(reservationPlanServiceDTO.getStartDate(), reservationPlanServiceDTO.getEndDate(), roomModel.getPricePerNight()))
                .startDate(reservationPlanServiceDTO.getStartDate())
                .endDate(reservationPlanServiceDTO.getEndDate())
                .reservationStatus(ReservationStatus.PLANNED)
                .build();
    }

    public ReservationModel reserveRoom(ReservationPlanServiceDTO reservationPlanServiceDTO, String userName) {
        List<Long> roomIds = getRoomsWithConditions(reservationPlanServiceDTO);
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

    private List<Long> getRoomsWithConditions(ReservationPlanServiceDTO reservationPlanServiceDTO) {
        return roomRepositoryService.getRoomsWithConditions(reservationPlanServiceDTO.getSingleBeds(),
                reservationPlanServiceDTO.getDoubleBeds(),
                reservationPlanServiceDTO.getRoomType(),
                reservationPlanServiceDTO.getHotelName(),
                reservationPlanServiceDTO.getCity());
    }
}
