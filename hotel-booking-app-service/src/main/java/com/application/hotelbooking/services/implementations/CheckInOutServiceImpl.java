package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.domain.ReservationStatus;
import com.application.hotelbooking.services.CheckInOutService;
import com.application.hotelbooking.services.repositoryservices.ReservationRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CheckInOutServiceImpl implements CheckInOutService {

    @Autowired
    private ReservationRepositoryService reservationRepositoryService;

    public ReservationModel getReservationDetails(UUID uuid) {
        return reservationRepositoryService.getReservationByUuid(uuid).get();
    }

    public ReservationModel checkInGuest(UUID uuid) {
        ReservationModel reservationModel = getReservationDetails(uuid);
        reservationModel.setReservationStatus(ReservationStatus.ACTIVE);
        return reservationRepositoryService.save(reservationModel);
    }

    public ReservationModel checkOutGuest(UUID uuid) {
        ReservationModel reservationModel = getReservationDetails(uuid);
        reservationModel.setReservationStatus(ReservationStatus.COMPLETED);
        return reservationRepositoryService.save(reservationModel);
    }
}
