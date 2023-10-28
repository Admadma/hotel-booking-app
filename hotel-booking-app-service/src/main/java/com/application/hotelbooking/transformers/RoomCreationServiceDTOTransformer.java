package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.dto.RoomCreationServiceDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomCreationServiceDTOTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public Room transformToRoom(RoomCreationServiceDTO roomCreationServiceDTO){
        return modelMapper.map(roomCreationServiceDTO, Room.class);
    }
}
