package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.dto.RoomCreationServiceDTO;
import com.application.hotelbooking.dto.RoomSearchFormServiceDTO;

import java.util.List;
import java.util.Optional;

public interface RoomRepositoryService {
    Optional<RoomModel> getRoomById(Long roomId);
    Optional<RoomModel> findRoomByNumberAndHotelName(int roomNumber, String hotelName);
    RoomModel saveRoom(RoomCreationServiceDTO roomCreationServiceDTO);
    void incrementRoomVersion(RoomModel roomModel);
    List<Long> getRoomsWithConditions(RoomSearchFormServiceDTO roomSearchFormServiceDTO);
}
