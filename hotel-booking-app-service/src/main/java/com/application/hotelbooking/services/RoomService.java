package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.dto.*;
import com.application.hotelbooking.exceptions.InvalidRoomException;
import com.application.hotelbooking.services.repositoryservices.RoomRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RoomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomService.class);
    public static final long DEFAULT_STARTING_VERSION = 1l;

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private RoomRepositoryService roomRepositoryService;

    @Autowired
    private ReservationService reservationService;


    private List<ReservableRoomDTO> createRoomSearchResultDTOs(List<Long> roomIds, RoomSearchFormServiceDTO roomSearchFormServiceDTO){
        List<ReservableRoomDTO> reservableRoomDTOS = new LinkedList<>();
        for (Long roomId : roomIds) {
            RoomModel room = roomRepositoryService.getRoomDTO(roomId);
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
                roomSearchFormServiceDTO.getStartDate(),
                roomSearchFormServiceDTO.getEndDate());
    }

    public List<ReservableRoomDTO> searchRooms(RoomSearchFormServiceDTO roomSearchFormServiceDTO){
        List<Long> roomIds = roomRepositoryService.getRoomsWithConditions(roomSearchFormServiceDTO);
        List<Long> availableRooms = filterAvailableRooms(roomSearchFormServiceDTO, roomIds);

        return createRoomSearchResultDTOs(availableRooms, roomSearchFormServiceDTO);
    }

    private List<Long> filterAvailableRooms(RoomSearchFormServiceDTO roomSearchFormServiceDTO, List<Long> roomIds) {
        return reservationService.filterFreeRooms(roomIds, roomSearchFormServiceDTO.getStartDate(), roomSearchFormServiceDTO.getEndDate());
    }

    public void createRoomFromDTO(RoomCreationServiceDTO roomCreationServiceDTO){
        if (roomRepositoryService.isRoomNumberFree(roomCreationServiceDTO.getRoomNumber())) {
            roomCreationServiceDTO.setVersion(DEFAULT_STARTING_VERSION);
            roomRepositoryService.saveRoom(roomCreationServiceDTO);
        } else {
            throw new InvalidRoomException("That roomNumber is already taken.");
        }
    }
}
