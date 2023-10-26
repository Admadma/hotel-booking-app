package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.Hotel;
import com.application.hotelbooking.domain.HotelModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelTransformer {
    @Autowired
    private ModelMapper modelMapper;

    public HotelModel transformToHotelModel(Hotel hotel){
        return modelMapper.map(hotel, HotelModel.class);
    }

    public Hotel transformToHotel(HotelModel hotelModel){
        return modelMapper.map(hotelModel, Hotel.class);
    }

    public List<HotelModel> transformToHotelModels(List<Hotel> hotels){
        return hotels.stream()
                .map(hotel -> modelMapper.map(hotel, HotelModel.class))
                .collect(Collectors.toList());
    }
}
