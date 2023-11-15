package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.dto.ReservableRoomDTO;
import com.application.hotelbooking.exceptions.OutdatedReservationException;
import com.application.hotelbooking.services.EmailSenderService;
import com.application.hotelbooking.services.ReservationService;
import com.application.hotelbooking.services.repositoryservices.ReservationRepositoryService;
import com.application.hotelbooking.services.repositoryservices.RoomRepositoryService;
import com.application.hotelbooking.services.repositoryservices.UserRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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
    private MessageSource messageSource;

    @Autowired
    private EmailSenderService emailSenderService;

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
        // While the user stayed on the confirm reservation page someone else might have created a reservation in the same time period.
        // This solution works for now. But later I could lock the room for example 15 minutes to give time for the user to confirm the reservation and enhance user experience.
        if (isRoomAvailableInTimePeriod(roomRepositoryService.findRoomByNumberAndHotelName(reservationModel.getRoom().getRoomNumber(), reservationModel.getRoom().getHotel().getHotelName()).getReservations(), reservationModel.getStartDate(), reservationModel.getEndDate())){
            ReservationModel reservation = reservationRepositoryService.save(reservationModel);
            sendReservationConfirmationEmail(reservation);
            return reservation;
        } else {
            throw new OutdatedReservationException("This reservation is no longer valid");
        }
    }

    private void sendReservationConfirmationEmail(ReservationModel reservationModel){
        Locale locale = LocaleContextHolder.getLocale();
        emailSenderService.sendEmail(reservationModel.getUser().getEmail(),
                messageSource.getMessage("email.reservation.confirmed.subject", null, locale),
                getBody(reservationModel, locale));
    }

    private String getBody(ReservationModel reservationModel, Locale locale) {
        return messageSource.getMessage("email.reservation.message", null, locale) +
                "<table border=\"1\" style=\"margin: auto;\">\n" +
                "        <tr>\n" +
                "            <td>" + messageSource.getMessage("email.reservation.roomNumber", null, locale) + "</td>\n" +
                "            <td>" + reservationModel.getRoom().getRoomNumber() + "</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>" + messageSource.getMessage("email.reservation.hotelName", null, locale) + "</td>\n" +
                "            <td>" + reservationModel.getRoom().getHotel().getHotelName() + "</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>" + messageSource.getMessage("email.reservation.city", null, locale) + "</td>\n" +
                "            <td>" + reservationModel.getRoom().getHotel().getCity() + "</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>" + messageSource.getMessage("email.reservation.roomType", null, locale) + "</td>\n" +
                "            <td>" + messageSource.getMessage("roomname." + reservationModel.getRoom().getRoomType().name().toLowerCase(), null, locale) + "</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>" + messageSource.getMessage("email.reservation.singleBeds", null, locale) + "</td>\n" +
                "            <td>" + reservationModel.getRoom().getSingleBeds() + "</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>" + messageSource.getMessage("email.reservation.doubleBeds", null, locale) + "</td>\n" +
                "            <td>" + reservationModel.getRoom().getDoubleBeds() + "</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>" + messageSource.getMessage("email.reservation.startDate", null, locale) + "</td>\n" +
                "            <td>" + reservationModel.getStartDate() + "</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>" + messageSource.getMessage("email.reservation.endDate", null, locale) + "</td>\n" +
                "            <td>" + reservationModel.getEndDate() + "</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>" + messageSource.getMessage("email.reservation.totalPrice", null, locale) + "</td>\n" +
                "            <td>" + reservationModel.getTotalPrice() + " HUF</td>\n" +
                "        </tr>\n" +
                "    </table>";
    }
}
