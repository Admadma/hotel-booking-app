package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.HotelModel;
import com.application.hotelbooking.domain.HotelView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelViewTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public HotelView transformToHotelView(HotelModel hotelModel){
        return modelMapper.map(hotelModel, HotelView.class);
    }

    public HotelModel transformToHotelModel(HotelView hotelView){
        return modelMapper.map(hotelView, HotelModel.class);
    }

    public List<HotelView> transformToHotelViews(List<HotelModel> hotelModels){
        return hotelModels.stream()
                .map(hotelModel -> modelMapper.map(hotelModel, HotelView.class))
                .collect(Collectors.toList());
    }
}
