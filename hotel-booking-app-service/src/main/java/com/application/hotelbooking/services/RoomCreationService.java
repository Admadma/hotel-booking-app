package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.dto.RoomCreationServiceDTO;

public interface RoomCreationService {
    RoomModel createRoomFromDTO(RoomCreationServiceDTO roomCreationServiceDTO);
}
