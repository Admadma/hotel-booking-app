package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.ReservationModel;

public interface ReservationConfirmationEmailService {
    void sendReservationConfirmationEmail(ReservationModel reservationModel);
}
