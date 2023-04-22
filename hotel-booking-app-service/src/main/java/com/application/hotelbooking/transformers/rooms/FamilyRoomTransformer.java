package com.application.hotelbooking.transformers.rooms;

import com.application.hotelbooking.domain.FamilyRoom;
import com.application.hotelbooking.domain.rooms.FamilyRoomModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FamilyRoomTransformer {
    @Autowired
    private ModelMapper modelMapper;

    public FamilyRoomModel transformToFamilyRoomModel(FamilyRoom familyRoom){
        return modelMapper.map(familyRoom, FamilyRoomModel.class);
    }

    public FamilyRoom transformToFamilyRoom(FamilyRoomModel familyRoomModel){
        return modelMapper.map(familyRoomModel, FamilyRoom.class);
    }
}
