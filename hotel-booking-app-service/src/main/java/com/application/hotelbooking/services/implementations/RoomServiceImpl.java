package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.dto.*;
import com.application.hotelbooking.services.HotelService;
import com.application.hotelbooking.services.ReservationService;
import com.application.hotelbooking.services.RoomService;
import com.application.hotelbooking.services.repositoryservices.RoomRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomServiceImpl.class);
    public static final long DEFAULT_STARTING_VERSION = 1l;

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private RoomRepositoryService roomRepositoryService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private HotelService hotelService;

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
        roomCreationServiceDTO.setVersion(DEFAULT_STARTING_VERSION);
        roomCreationServiceDTO.setRoomNumber(1 + hotelService.getLatestRoomNumberOfHotel(roomCreationServiceDTO.getHotelId()));
        roomRepositoryService.saveRoom(roomCreationServiceDTO);
    }
}
