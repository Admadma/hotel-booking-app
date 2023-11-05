package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.repositories.ReservationRepository;
import com.application.hotelbooking.transformers.ReservationTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationRepositoryService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTransformer reservationTransformer;

    public ReservationModel save(ReservationModel reservationModel){
        return reservationTransformer.transformToReservationModel(reservationRepository.save(reservationTransformer.transformToReservation(reservationModel)));
    }

    public List<ReservationModel> getReservationsByRoomId(Long roomId){
        return reservationTransformer.transformToReservationModels(reservationRepository.findAllByRoomId(roomId));
    }
}
