package com.application.hotelbooking.services;

import com.application.hotelbooking.models.ReservationModel;

import java.util.UUID;

public interface CheckInOutService {

    ReservationModel getReservationDetails(UUID uuid);
    ReservationModel checkInGuest(UUID uuid);
    ReservationModel checkOutGuest(UUID uuid);
}
