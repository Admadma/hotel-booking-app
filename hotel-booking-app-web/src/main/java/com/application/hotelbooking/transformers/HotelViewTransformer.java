package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.HotelModel;
import com.application.hotelbooking.domain.HotelView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelViewTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public HotelView transformToRoomView(HotelModel hotelModel){
        return modelMapper.map(hotelModel, HotelView.class);
    }

    public HotelModel transformToRoomModel(HotelView hotelView){
        return modelMapper.map(hotelView, HotelModel.class);
    }
}
