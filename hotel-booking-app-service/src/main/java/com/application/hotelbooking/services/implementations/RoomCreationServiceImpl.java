package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.dto.RoomCreationServiceDTO;
import com.application.hotelbooking.services.HotelService;
import com.application.hotelbooking.services.RoomCreationService;
import com.application.hotelbooking.services.repositoryservices.RoomRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomCreationServiceImpl implements RoomCreationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomCreationServiceImpl.class);

    public static final long DEFAULT_STARTING_VERSION = 1l;

    @Autowired
    private RoomRepositoryService roomRepositoryService;

    @Autowired
    private HotelService hotelService;

    public RoomModel createRoomFromDTO(RoomCreationServiceDTO roomCreationServiceDTO){
        roomCreationServiceDTO.setVersion(DEFAULT_STARTING_VERSION);
        roomCreationServiceDTO.setRoomNumber(1 + hotelService.getLatestRoomNumberOfHotel(roomCreationServiceDTO.getHotelId()));
        RoomModel savedRoom = roomRepositoryService.saveRoom(roomCreationServiceDTO);
        LOGGER.info("Saved room with roomNumber: " + savedRoom.getRoomNumber());
        return savedRoom;
    }
}
