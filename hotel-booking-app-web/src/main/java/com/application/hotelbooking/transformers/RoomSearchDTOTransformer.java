package com.application.hotelbooking.transformers;

import com.application.hotelbooking.dto.RoomSearchFormDTO;
import com.application.hotelbooking.dto.RoomSearchFormServiceDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomSearchDTOTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public RoomSearchFormServiceDTO transformToRoomSearchFormServiceDTO(RoomSearchFormDTO roomSearchFormDTO){
        return modelMapper.map(roomSearchFormDTO, RoomSearchFormServiceDTO.class);
    }
}
