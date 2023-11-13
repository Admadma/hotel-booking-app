package com.application.hotelbooking.services.repositoryservices.implementations;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.repositories.ReservationRepository;
import com.application.hotelbooking.services.repositoryservices.ReservationRepositoryService;
import com.application.hotelbooking.transformers.ReservationTransformer;
import com.application.hotelbooking.transformers.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationRepositoryServiceImpl implements ReservationRepositoryService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTransformer reservationTransformer;

    @Autowired
    private UserTransformer userTransformer;

    public ReservationModel save(ReservationModel reservationModel){
        return reservationTransformer.transformToReservationModel(reservationRepository.save(reservationTransformer.transformToReservation(reservationModel)));
    }

    public List<ReservationModel> getReservationsByRoomId(Long roomId){
        return reservationTransformer.transformToReservationModels(reservationRepository.findAllByRoomId(roomId));
    }

    public List<ReservationModel> getReservationsByUser(UserModel userModel){
        return reservationTransformer.transformToReservationModels(reservationRepository.findAllByUser(userTransformer.transformToUser(userModel)));
    }

    public void delete(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
