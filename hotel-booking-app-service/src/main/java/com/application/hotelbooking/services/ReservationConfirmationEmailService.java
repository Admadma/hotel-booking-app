package com.application.hotelbooking.services;

import com.application.hotelbooking.models.ReservationModel;

public interface ReservationConfirmationEmailService {
    void sendReservationConfirmationEmail(ReservationModel reservationModel);
}
