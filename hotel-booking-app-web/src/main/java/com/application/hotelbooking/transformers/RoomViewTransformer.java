package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.domain.RoomView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
