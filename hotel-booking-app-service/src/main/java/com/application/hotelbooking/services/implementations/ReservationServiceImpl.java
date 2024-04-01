package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.domain.ReservationStatus;
import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.dto.HotelWithReservableRoomsServiceDTO;
import com.application.hotelbooking.dto.ReservableRoomDTO;
import com.application.hotelbooking.dto.UniqueReservableRoomOfHotelServiceDTO;
import com.application.hotelbooking.exceptions.OutdatedReservationException;
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
import java.util.LinkedList;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);
    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private ReservationRepositoryService reservationRepositoryService;

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
        return pricePerNight * (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    public ReservationModel prepareReservation(ReservableRoomDTO reservableRoomDTO, String userName){
        return ReservationModel.builder()
                .room(roomRepositoryService.findRoomByNumberAndHotelName(reservableRoomDTO.getRoomNumber(), reservableRoomDTO.getHotelName()).get())
                .user(userRepositoryService.getUserByName(userName).get())
                .totalPrice(reservableRoomDTO.getTotalPrice())
                .startDate(reservableRoomDTO.getStartDate())
                .endDate(reservableRoomDTO.getEndDate())
                .reservationStatus(ReservationStatus.PLANNED)
                .build();
    }

    public ReservationModel prepareReservationNew(String hotelName, List<HotelWithReservableRoomsServiceDTO> hotelWithReservableRoomsServiceDTOS, String userName){
        HotelWithReservableRoomsServiceDTO hotelWithReservableRoomsServiceDTO = hotelWithReservableRoomsServiceDTOS.stream().filter(hotel -> hotel.getHotelName().equals(hotelName)).findFirst().get();

        for (UniqueReservableRoomOfHotelServiceDTO uniqueReservableRoomOfHotelServiceDTO : hotelWithReservableRoomsServiceDTO.getUniqueReservableRoomOfHotelServiceDTOList()){
            RoomModel roomModel = roomRepositoryService.findRoomByNumberAndHotelName(uniqueReservableRoomOfHotelServiceDTO.getNumber(), hotelName).get();

            if (isRoomAvailableInTimePeriod(roomModel.getReservations(), uniqueReservableRoomOfHotelServiceDTO.getStartDate(), uniqueReservableRoomOfHotelServiceDTO.getEndDate())){
                return ReservationModel.builder()
                        .uuid(uuidWrapper.getRandomUUID())
                        .room(roomModel)
                        .user(userRepositoryService.getUserByName(userName).get())
                        .totalPrice(uniqueReservableRoomOfHotelServiceDTO.getTotalPrice())
                        .startDate(uniqueReservableRoomOfHotelServiceDTO.getStartDate())
                        .endDate(uniqueReservableRoomOfHotelServiceDTO.getEndDate())
                        .reservationStatus(ReservationStatus.PLANNED)
                        .build();
            }
        }

        throw new OutdatedReservationException("No more rooms available with these parameters.");
    }

    /**
     *         While the user stayed on the confirm reservation page someone else might have created a reservation in the same time period.
     *         This solution works for now. But later I could lock the room for example 15 minutes to give time for the user to confirm the reservation and enhance user experience.
     * @param reservationModel a reservation prepared by UserService.prepareReservation, with attributes provided by the user
     * @return returns the reservation that was saved to the database.
     */
    public ReservationModel reserveRoom(ReservationModel reservationModel){
        // By checking that the version is still the same, I can guarantee that the selected time period had no new reservations, and I can skip checking it all again
        if (isRoomVersionUnchanged(reservationModel) || isRoomAvailableInTimePeriod(roomRepositoryService.findRoomByNumberAndHotelName(reservationModel.getRoom().getRoomNumber(), reservationModel.getRoom().getHotel().getHotelName()).get().getReservations(), reservationModel.getStartDate(), reservationModel.getEndDate())){
            ReservationModel reservation = reservationRepositoryService.save(reservationModel);
            roomRepositoryService.incrementRoomVersion(reservation.getRoom());
            reservationConfirmationEmailService.sendReservationConfirmationEmail(reservation);
            return reservation;
        } else {
            throw new OutdatedReservationException("This reservation is no longer valid because the room has been updated");
        }
    }

    private boolean isRoomVersionUnchanged(ReservationModel reservationModel) {
        return reservationModel.getRoom().getVersion() == roomRepositoryService.findRoomByNumberAndHotelName(reservationModel.getRoom().getRoomNumber(), reservationModel.getRoom().getHotel().getHotelName()).get().getVersion();
    }
}
