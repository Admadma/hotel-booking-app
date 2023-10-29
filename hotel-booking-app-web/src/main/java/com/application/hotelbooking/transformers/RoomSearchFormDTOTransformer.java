package com.application.hotelbooking.transformers;

import com.application.hotelbooking.dto.RoomSearchFormServiceDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomSearchFormDTOTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public RoomSearchFormServiceDTO transformToRoomSearchFormServiceDTO(RoomSearchFormServiceDTO roomSearchFormServiceDTO){
        return modelMapper.map(roomSearchFormServiceDTO, RoomSearchFormServiceDTO.class);
    }
}
