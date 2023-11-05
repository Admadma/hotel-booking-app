package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.ReservationModel;
import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.dto.ReservableRoomDTO;
import com.application.hotelbooking.exceptions.InvalidTimePeriodException;
import com.application.hotelbooking.services.repositoryservices.ReservationRepositoryService;
import com.application.hotelbooking.services.repositoryservices.RoomRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);
    @Autowired
    private UserService userService;

    @Autowired
    private ReservationRepositoryService reservationRepositoryService;

    @Autowired
    private RoomRepositoryService roomRepositoryService;

    private boolean isRoomAvailableInTimePeriod(List<ReservationModel> reservations, LocalDate selectedStartDate, LocalDate selectedEndDate){
        for (ReservationModel reservation : reservations) {
            if (!(reservation.getStartDate().plusDays(1).isAfter(selectedEndDate) || reservation.getEndDate().minusDays(1).isBefore(selectedStartDate))) {
                // I check each reservation of this room. If it has a single conflict then I can't reserve this in the selected time period.
                return false;
            }
        }
        return true;
    }

    public List<Long> filterFreeRooms(List<Long> roomIds, LocalDate startDate, LocalDate endDate){
        List<Long> freeRooms = new LinkedList<>();
        List<ReservationModel> reservations;

        for (Long roomId : roomIds) {
            reservations = reservationRepositoryService.getReservationsByRoomId(roomId);
            if (reservations.isEmpty() || isRoomAvailableInTimePeriod(reservations, startDate, endDate)){
                freeRooms.add(roomId);
            }
        }

        return freeRooms;
    }

    public ReservationModel prepareReservation(ReservableRoomDTO reservableRoomDTO, String userName){
        return ReservationModel.builder()
                .room(roomRepositoryService.findRoomByNumberAndHotelName(reservableRoomDTO.getRoomNumber(), reservableRoomDTO.getHotelName()))
                .user(userService.getUsersByName(userName).get(0))
                .startDate(reservableRoomDTO.getStartDate())
                .endDate(reservableRoomDTO.getEndDate())
                .build();
    }

    public ReservationModel reserveRoom(ReservationModel reservationModel){
        //TODO: validatie version or availability
        return reservationRepositoryService.save(reservationModel);
    }

    //TODO: rename the RoomSearchResultDTO class if I want to use it for other purpose (like here) than returning the search result
    //TODO: In the old version I checked the version on the confirmation page. Now there is no reason for it, since I basically check the entire input data here and reserve that exact room, so I can handle version change due to availability errors here
    public boolean reserveRoom(LocalDate startDate, LocalDate endDate, RoomModel roomModel, String username){
        if (!isRoomAvailableInTimePeriod(roomModel.getReservations(),  startDate, endDate)){
            throw new InvalidTimePeriodException();
        }
        ReservationModel reservationModel = ReservationModel
                .builder()
                .room(roomModel)
                .user(userService.getUsersByName(username).get(0))
                .startDate(startDate)
                .endDate(endDate)
                .build();


        ReservationModel reservation = reservationRepositoryService.save(reservationModel);

        if (Objects.isNull(reservation)){
            return false;
        }
        LOGGER.info(reservation.getStartDate().toString());
        LOGGER.info(String.valueOf(reservation.getRoom().getRoomNumber()));
        return true;
    }
}
