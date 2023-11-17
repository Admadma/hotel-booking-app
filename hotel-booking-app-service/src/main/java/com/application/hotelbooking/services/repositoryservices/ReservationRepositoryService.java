package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.domain.UserModel;

import java.util.List;

public interface ReservationRepositoryService {
    ReservationModel save(ReservationModel reservationModel);
    List<ReservationModel> getReservationsByRoomId(Long roomId);
    List<ReservationModel> getReservationsByUserId(Long userId);
    void delete(Long reservationId);
}
