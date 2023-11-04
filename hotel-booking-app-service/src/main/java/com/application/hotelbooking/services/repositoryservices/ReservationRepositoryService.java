package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.dto.ReservationDTO;
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

    public ReservationDTO save(ReservationDTO reservationDTO){
        return reservationTransformer.transformToReservationDTO(reservationRepository.save(reservationTransformer.transformToReservation(reservationDTO)));
    }

    public List<ReservationDTO> getReservationsByRoomId(Long roomId){
        return reservationTransformer.transformToReservationDTOs(reservationRepository.findAllByRoomId(roomId));
    }
}
