package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.HotelView;
import com.application.hotelbooking.domain.HotelModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelViewTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public HotelModel transformToHotelDTO(HotelView hotelView){
        return modelMapper.map(hotelView, HotelModel.class);
    }

    public List<HotelView> transformToHotelViews(List<HotelModel> hotelModels){
        return hotelModels.stream()
                .map(hotelModel -> modelMapper.map(hotelModel, HotelView.class))
                .collect(Collectors.toList());
    }
}
