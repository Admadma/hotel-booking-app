package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.*;
import com.application.hotelbooking.dto.RoomCreationServiceDTO;
import com.application.hotelbooking.domain.RoomModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public RoomModel transformToRoomModel(Room room){
        return modelMapper.map(room, RoomModel.class);
    }

    public Room transformToRoom(RoomCreationServiceDTO roomCreationServiceDTO){
        modelMapper.typeMap(RoomCreationServiceDTO.class, Room.class).addMappings(modelMapper -> modelMapper.skip(Room::setId));
        return modelMapper.map(roomCreationServiceDTO, Room.class);
    }
}
