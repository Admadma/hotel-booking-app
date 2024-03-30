package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.dto.*;
import com.application.hotelbooking.services.ReservationService;
import com.application.hotelbooking.services.RoomService;
import com.application.hotelbooking.services.repositoryservices.RoomRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomServiceImpl.class);

    @Autowired
    private RoomRepositoryService roomRepositoryService;

    @Autowired
    private ReservationService reservationService;

    private List<ReservableRoomDTO> createRoomSearchResultDTOs(List<Long> roomIds, RoomSearchFormServiceDTO roomSearchFormServiceDTO){
        List<ReservableRoomDTO> reservableRoomDTOS = new LinkedList<>();
        for (Long roomId : roomIds) {
            RoomModel room = roomRepositoryService.getRoomById(roomId).get();
            reservableRoomDTOS.add(createReservableRoomDTO(room, roomSearchFormServiceDTO));
        }
        return reservableRoomDTOS;
    }

    private ReservableRoomDTO createReservableRoomDTO(RoomModel room, RoomSearchFormServiceDTO roomSearchFormServiceDTO) {
        return new ReservableRoomDTO(room.getRoomNumber(),
                room.getSingleBeds(),
                room.getDoubleBeds(),
                reservationService.calculateTotalPrice(
                        roomSearchFormServiceDTO.getStartDate(),
                        roomSearchFormServiceDTO.getEndDate(),
                        room.getPricePerNight()),
                room.getRoomType(),
                room.getHotel().getHotelName(),
                room.getHotel().getCity(),
                room.getHotel().getImageName(),
                roomSearchFormServiceDTO.getStartDate(),
                roomSearchFormServiceDTO.getEndDate());
    }

    private List<Long> filterAvailableRooms(RoomSearchFormServiceDTO roomSearchFormServiceDTO, List<Long> roomIds) {
        return reservationService.filterFreeRooms(roomIds, roomSearchFormServiceDTO.getStartDate(), roomSearchFormServiceDTO.getEndDate());
    }

    public List<ReservableRoomDTO> searchRooms(RoomSearchFormServiceDTO roomSearchFormServiceDTO){
        List<Long> roomIds = roomRepositoryService.getRoomsWithConditions(roomSearchFormServiceDTO);
        List<Long> availableRoomsIds = filterAvailableRooms(roomSearchFormServiceDTO, roomIds);

        return createRoomSearchResultDTOs(availableRoomsIds, roomSearchFormServiceDTO);
    }

    public boolean isEndDateAfterStartDate(LocalDate startDate, LocalDate endDate){
        return endDate.isAfter(startDate);
    }
}
