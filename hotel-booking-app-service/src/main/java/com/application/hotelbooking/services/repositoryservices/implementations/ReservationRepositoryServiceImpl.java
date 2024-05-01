package com.application.hotelbooking.services.repositoryservices.implementations;

import com.application.hotelbooking.models.ReservationModel;
import com.application.hotelbooking.repositories.ReservationRepository;
import com.application.hotelbooking.services.repositoryservices.ReservationRepositoryService;
import com.application.hotelbooking.transformers.ReservationTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationRepositoryServiceImpl implements ReservationRepositoryService {

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

    public List<ReservationModel> getReservationsByUserId(Long userId){
        return reservationTransformer.transformToReservationModels(reservationRepository.findAllByUserId(userId));
    }

    public Optional<ReservationModel> getReservationByUuid(UUID uuid) {
        return reservationTransformer.transformToOptionalReservationModel(reservationRepository.findByUuid(uuid));
    }

    public void delete(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
