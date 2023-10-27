package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.domain.RoomView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomViewTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public RoomView transformToRoomView(RoomModel roomModel){
        return modelMapper.map(roomModel, RoomView.class);
    }

    public RoomModel transformToRoomModel(RoomView roomView){
        return modelMapper.map(roomView, RoomModel.class);
    }

    public List<RoomView> transformToRoomViews(List<RoomModel> roomModels){
        return roomModels.stream()
                .map(roomModel -> modelMapper.map(roomModel, RoomView.class))
                .collect(Collectors.toList());
    }

//    public List<RoomModel> transformToRoomModels(List<Room> rooms){
//        return rooms.stream()
//                .map(room -> modelMapper.map(room, RoomModel.class))
//                .collect(Collectors.toList());
//    }
}
