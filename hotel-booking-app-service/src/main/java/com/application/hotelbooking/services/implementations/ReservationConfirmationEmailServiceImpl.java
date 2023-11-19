package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.services.EmailSenderService;
import com.application.hotelbooking.services.ReservationConfirmationEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ReservationConfirmationEmailServiceImpl implements ReservationConfirmationEmailService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private EmailSenderService emailSenderService;

    public void sendReservationConfirmationEmail(ReservationModel reservationModel){
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
