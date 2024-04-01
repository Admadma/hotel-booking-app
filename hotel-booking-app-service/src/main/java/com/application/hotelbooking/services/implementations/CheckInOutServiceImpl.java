package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.domain.ReservationModel;
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
}
