package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.Hotel;
import com.application.hotelbooking.domain.HotelModel;
import com.application.hotelbooking.dto.HotelCreationServiceDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HotelTransformer {
    @Autowired
    private ModelMapper modelMapper;

    public Hotel transformToHotel(HotelCreationServiceDTO hotelCreationServiceDTO){
        return modelMapper.map(hotelCreationServiceDTO, Hotel.class);
    }

    public HotelModel transformToHotelModel(Hotel hotel){
        return modelMapper.map(hotel, HotelModel.class);
    }

    public Optional<HotelModel> transformToOptionalHotelModel(Optional<Hotel> hotel){
        if (hotel.isPresent()){
            return Optional.of(modelMapper.map(hotel, HotelModel.class));
        } else {
            return Optional.empty();
        }
    }

    public List<HotelModel> transformToHotelModels(List<Hotel> hotels){
        return hotels.stream()
                .map(hotel -> modelMapper.map(hotel, HotelModel.class))
                .collect(Collectors.toList());
    }
}
