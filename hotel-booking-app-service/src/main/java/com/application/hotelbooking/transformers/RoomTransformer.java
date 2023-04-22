package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public RoomModel transformToRoomModel(Room room){
        return modelMapper.map(room, RoomModel.class);
    }

    public Room transformToRoom(RoomModel roomModel){
        return modelMapper.map(roomModel, Room.class);
    }

    public List<RoomModel> transformToRoomModels(List<Room> rooms){
        return rooms.stream()
                .map(room -> modelMapper.map(room, RoomModel.class))
                .collect(Collectors.toList());
    }
}
