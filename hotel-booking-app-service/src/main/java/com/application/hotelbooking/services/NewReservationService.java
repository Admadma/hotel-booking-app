package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.dto.ReservationDTO;
import com.application.hotelbooking.dto.RoomDTO;
import com.application.hotelbooking.dto.RoomSearchResultDTO;
import com.application.hotelbooking.exceptions.InvalidTimePeriodException;
import com.application.hotelbooking.repositories.ReservationRepository;
import com.application.hotelbooking.transformers.ReservationTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class NewReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewReservationService.class);
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTransformer reservationTransformer;

    private boolean isRoomAvailableInTimePeriod(List<ReservationModel> reservations, LocalDate selectedStartDate, LocalDate selectedEndDate){
        for (ReservationModel reservation : reservations) {
            if (!(reservation.getStartDate().isAfter(selectedEndDate) || reservation.getEndDate().minusDays(1).isBefore(selectedStartDate))) {
                // I check each reservation of this room. If it has a single conflict then I can't reserve this in the selected time period.
                return false;
            }
        }
        return true;
    }

    private int isRoomAvailableInTimePeriodInt(List<ReservationDTO> reservations, LocalDate selectedStartDate, LocalDate selectedEndDate){
        for (ReservationDTO reservation : reservations) {
            if (!(reservation.getStartDate().isAfter(selectedEndDate) || reservation.getEndDate().minusDays(1).isBefore(selectedStartDate))) {
                // I check each reservation of this room. If it has a single conflict then I can't reserve this in the selected time period.
                return 0;
            }
        }
        return 1;
    }

    public List<Long> filterFreeRooms(List<Long> roomIds, LocalDate startDate, LocalDate endDate){
        List<Long> freeRooms = new LinkedList<>();
        List<ReservationModel> reservations;

        for (Long roomId : roomIds) {
            reservations = reservationTransformer.transformToReservationModels(reservationRepository.findAllByRoomId(roomId));
            if (reservations.isEmpty() || isRoomAvailableInTimePeriod(reservations, startDate, endDate)){
                freeRooms.add(roomId);
            }
        }

        return freeRooms;
    }

    //TODO: rename the RoomSearchResultDTO class if I want to use it for other purpose (like here) than returning the search result
    //TODO: In the old version I checked the version on the confirmation page. Now there is no reason for it, since I basically check the entire input data here and reserve that exact room, so I can handle version change due to availability errors here
    public boolean reserveRoom(LocalDate startDate, LocalDate endDate, RoomDTO roomDTO, String user){
        if (0 == isRoomAvailableInTimePeriodInt(roomDTO.getReservations(),  startDate, endDate)){
            throw new InvalidTimePeriodException();
        }
        ReservationDTO reservationDTO = ReservationDTO
                .builder()
                .room(roomDTO)
                .user(null)
                .startDate(startDate)
                .endDate(endDate)
                .build();


        ReservationDTO reservation = reservationTransformer.transformToReservationDTO(reservationRepository.save(reservationTransformer.transformToReservation(reservationDTO)));

        if (Objects.isNull(reservation)){
            return false;
        }
        LOGGER.info(reservation.getStartDate().toString());
        LOGGER.info(String.valueOf(reservation.getRoom().getRoomNumber()));
        return true;
    }
}
