package com.application.hotelbooking.transformers.room;

import com.application.hotelbooking.domain.rooms.FamilyRoomModel;
import com.application.hotelbooking.domain.rooms.FamilyRoomView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FamilyRoomViewTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public FamilyRoomView transformToFamilyRoomView(FamilyRoomModel familyRoomModel){
        return modelMapper.map(familyRoomModel, FamilyRoomView.class);
    }

    public FamilyRoomModel transformToFamilyRoomModel(FamilyRoomView familyRoomView){
        return modelMapper.map(familyRoomView, FamilyRoomModel.class);
    }
}
