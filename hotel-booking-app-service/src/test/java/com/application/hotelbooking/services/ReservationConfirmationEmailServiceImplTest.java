package com.application.hotelbooking.services;

import com.application.hotelbooking.models.*;
import com.application.hotelbooking.services.implementations.ReservationConfirmationEmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReservationConfirmationEmailServiceImplTest {

    private static final UUID RESERVATION_UUID = UUID.randomUUID();
    private static final String EMAIL = "test_email";
    private static final String RESERVATION_SUBJECT_CODE = "email.reservation.confirmed.subject";
    private static final String RESERVATION_SUBJECT = "test_subject_successful_reservation";
    private static final String RESERVATION_MESSAGE_CODE = "email.reservation.message";
    private static final String RESERVATION_MESSAGE = "test_body_successful_reservation";
    private static final String RESERVATION_ROOM_NUMBER_CODE = "email.reservation.roomNumber";
    private static final String RESERVATION_ROOM_NUMBER = "Room number";
    private static final String RESERVATION_HOTEL_NAME_CODE = "email.reservation.hotelName";
    private static final String RESERVATION_HOTEL_NAME = "Hotel name";
    private static final String RESERVATION_CITY_CODE = "email.reservation.city";
    private static final String RESERVATION_CITY = "City";
    private static final String RESERVATION_ROOM_TYPE_CODE = "email.reservation.roomType";
    private static final String RESERVATION_ROOM_TYPE = "Room type";
    private static final String RESERVED_ROOM_TYPE_CODE = "roomname.family_room";
    private static final String RESERVED_ROOM_TYPE = "Family room";
    private static final String RESERVATION_SINGLE_BEDS_CODE = "email.reservation.singleBeds";
    private static final String RESERVATION_SINGLE_BEDS = "Single beds";
    private static final String RESERVATION_DOUBLE_BEDS_CODE = "email.reservation.doubleBeds";
    private static final String RESERVATION_DOUBLE_BEDS = "Double beds";
    private static final String RESERVATION_START_DATE_CODE = "email.reservation.startDate";
    private static final String RESERVATION_START_DATE= "Start date";
    private static final String RESERVATION_END_DATE_CODE = "email.reservation.endDate";
    private static final String RESERVATION_END_DATE= "End date";
    private static final String RESERVATION_TOTAL_PRICE_CODE = "email.reservation.totalPrice";
    private static final String RESERVATION_TOTAL_PRICE= "Total price";
    private static final String RESERVATION_ID_CODE = "email.reservation.id";
    private static final String RESERVATION_ID= "Reservation ID";
    private static final UserModel USER_MODEL = UserModel.builder()
            .email(EMAIL)
            .build();
    private static final HotelModel HOTEL_MODEL = HotelModel.builder()
            .hotelName("Test hotel")
            .city("Test city")
            .build();
    private static final RoomModel ROOM_MODEL = RoomModel.builder()
            .roomNumber(1)
            .hotel(HOTEL_MODEL)
            .singleBeds(2)
            .doubleBeds(1)
            .roomType(RoomType.FAMILY_ROOM)
            .build();
    private static final ReservationModel RESERVATION_MODEL = ReservationModel.builder()
            .uuid(RESERVATION_UUID)
            .room(ROOM_MODEL)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(5))
            .totalPrice(100)
            .user(USER_MODEL)
            .build();
    private static final String FULL_BODY = RESERVATION_MESSAGE +
            "<table border=\"1\" style=\"margin: auto;\">\n" +
            "        <tr>\n" +
            "            <td>" + RESERVATION_ROOM_NUMBER + "</td>\n" +
            "            <td>" + RESERVATION_MODEL.getRoom().getRoomNumber() + "</td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td>" + RESERVATION_HOTEL_NAME + "</td>\n" +
            "            <td>" + RESERVATION_MODEL.getRoom().getHotel().getHotelName() + "</td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td>" + RESERVATION_CITY + "</td>\n" +
            "            <td>" + RESERVATION_MODEL.getRoom().getHotel().getCity() + "</td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td>" + RESERVATION_ROOM_TYPE + "</td>\n" +
            "            <td>" + RESERVED_ROOM_TYPE + "</td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td>" + RESERVATION_SINGLE_BEDS + "</td>\n" +
            "            <td>" + RESERVATION_MODEL.getRoom().getSingleBeds() + "</td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td>" + RESERVATION_DOUBLE_BEDS + "</td>\n" +
            "            <td>" + RESERVATION_MODEL.getRoom().getDoubleBeds() + "</td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td>" + RESERVATION_START_DATE + "</td>\n" +
            "            <td>" + RESERVATION_MODEL.getStartDate() + "</td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td>" + RESERVATION_END_DATE + "</td>\n" +
            "            <td>" + RESERVATION_MODEL.getEndDate() + "</td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td>" + RESERVATION_TOTAL_PRICE + "</td>\n" +
            "            <td>" + RESERVATION_MODEL.getTotalPrice() + " HUF</td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td>" + RESERVATION_ID + "</td>\n" +
            "            <td>" + RESERVATION_MODEL.getUuid() + "</td>\n" +
            "        </tr>\n" +
            "    </table>";;

    @InjectMocks
    private ReservationConfirmationEmailServiceImpl reservationConfirmationEmailService;

    @Mock
    private MessageSource messageSource;

    @Mock
    private EmailSenderService emailSenderService;


    @Test
    public void testSendReservationConfirmationEmailShouldCallSendEmailWithProperContent(){
        when(messageSource.getMessage(eq(RESERVATION_SUBJECT_CODE), eq(null), any(Locale.class))).thenReturn(RESERVATION_SUBJECT);
        when(messageSource.getMessage(eq(RESERVATION_MESSAGE_CODE), eq(null), any(Locale.class))).thenReturn(RESERVATION_MESSAGE);
        when(messageSource.getMessage(eq(RESERVATION_ROOM_NUMBER_CODE), eq(null), any(Locale.class))).thenReturn(RESERVATION_ROOM_NUMBER);
        when(messageSource.getMessage(eq(RESERVATION_HOTEL_NAME_CODE), eq(null), any(Locale.class))).thenReturn(RESERVATION_HOTEL_NAME);
        when(messageSource.getMessage(eq(RESERVATION_CITY_CODE), eq(null), any(Locale.class))).thenReturn(RESERVATION_CITY);
        when(messageSource.getMessage(eq(RESERVATION_ROOM_TYPE_CODE), eq(null), any(Locale.class))).thenReturn(RESERVATION_ROOM_TYPE);
        when(messageSource.getMessage(eq(RESERVED_ROOM_TYPE_CODE), eq(null), any(Locale.class))).thenReturn(RESERVED_ROOM_TYPE);
        when(messageSource.getMessage(eq(RESERVATION_SINGLE_BEDS_CODE), eq(null), any(Locale.class))).thenReturn(RESERVATION_SINGLE_BEDS);
        when(messageSource.getMessage(eq(RESERVATION_DOUBLE_BEDS_CODE), eq(null), any(Locale.class))).thenReturn(RESERVATION_DOUBLE_BEDS);
        when(messageSource.getMessage(eq(RESERVATION_START_DATE_CODE), eq(null), any(Locale.class))).thenReturn(RESERVATION_START_DATE);
        when(messageSource.getMessage(eq(RESERVATION_END_DATE_CODE), eq(null), any(Locale.class))).thenReturn(RESERVATION_END_DATE);
        when(messageSource.getMessage(eq(RESERVATION_TOTAL_PRICE_CODE), eq(null), any(Locale.class))).thenReturn(RESERVATION_TOTAL_PRICE);
        when(messageSource.getMessage(eq(RESERVATION_ID_CODE), eq(null), any(Locale.class))).thenReturn(RESERVATION_ID);
        doNothing().when(emailSenderService).sendEmail(EMAIL, RESERVATION_SUBJECT, FULL_BODY);

        reservationConfirmationEmailService.sendReservationConfirmationEmail(RESERVATION_MODEL);

        verify(messageSource).getMessage(eq(RESERVATION_SUBJECT_CODE), eq(null), any(Locale.class));
        verify(messageSource).getMessage(eq(RESERVATION_MESSAGE_CODE), eq(null), any(Locale.class));
        verify(messageSource).getMessage(eq(RESERVATION_ROOM_NUMBER_CODE), eq(null), any(Locale.class));
        verify(messageSource).getMessage(eq(RESERVATION_HOTEL_NAME_CODE), eq(null), any(Locale.class));
        verify(messageSource).getMessage(eq(RESERVATION_CITY_CODE), eq(null), any(Locale.class));
        verify(messageSource).getMessage(eq(RESERVATION_ROOM_TYPE_CODE), eq(null), any(Locale.class));
        verify(messageSource).getMessage(eq(RESERVED_ROOM_TYPE_CODE), eq(null), any(Locale.class));
        verify(messageSource).getMessage(eq(RESERVATION_SINGLE_BEDS_CODE), eq(null), any(Locale.class));
        verify(messageSource).getMessage(eq(RESERVATION_DOUBLE_BEDS_CODE), eq(null), any(Locale.class));
        verify(messageSource).getMessage(eq(RESERVATION_START_DATE_CODE), eq(null), any(Locale.class));
        verify(messageSource).getMessage(eq(RESERVATION_END_DATE_CODE), eq(null), any(Locale.class));
        verify(messageSource).getMessage(eq(RESERVATION_TOTAL_PRICE_CODE), eq(null), any(Locale.class));
        verify(messageSource).getMessage(eq(RESERVATION_ID_CODE), eq(null), any(Locale.class));
        verify(emailSenderService).sendEmail(EMAIL, RESERVATION_SUBJECT, FULL_BODY);
    }
}
