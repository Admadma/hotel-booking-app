package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.dto.RoomCreationServiceDTO;
import com.application.hotelbooking.dto.RoomSearchFormServiceDTO;

import java.util.List;

public interface RoomRepositoryService {
    RoomModel getRoomDTO(Long roomId);
    RoomModel findRoomByNumberAndHotelName(int roomNumber, String hotelName);
    RoomModel saveRoom(RoomCreationServiceDTO roomCreationServiceDTO);
    RoomModel updateRoom(RoomModel roomModel);
    List<Long> getRoomsWithConditions(RoomSearchFormServiceDTO roomSearchFormServiceDTO);
}
