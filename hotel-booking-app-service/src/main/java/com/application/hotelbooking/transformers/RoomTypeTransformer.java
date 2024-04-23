package com.application.hotelbooking.transformers;

import com.application.hotelbooking.models.RoomType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RoomTypeTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public com.application.hotelbooking.entities.RoomType transformToRoomTypeEntity(RoomType roomType){
        if (Objects.isNull(roomType)) {
            return null;
        } else {
            return modelMapper.map(roomType, com.application.hotelbooking.entities.RoomType.class);
        }
    }
}
