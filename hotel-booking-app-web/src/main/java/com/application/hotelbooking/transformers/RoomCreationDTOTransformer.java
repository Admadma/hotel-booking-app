package com.application.hotelbooking.transformers;

import com.application.hotelbooking.dto.RoomCreationDTO;
import com.application.hotelbooking.dto.RoomCreationServiceDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomCreationDTOTransformer {

    @Autowired
    private ModelMapper modelMapper;

    //TODO: Do I want a separate transformer class for each DTO type or just one for each group (Room, Hotel, ...)
    public RoomCreationServiceDTO transformToRoomCreationServiceDTO(RoomCreationDTO roomCreationDTO){
        return modelMapper.map(roomCreationDTO, RoomCreationServiceDTO.class);
    }
}
