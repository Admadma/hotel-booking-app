package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.Hotel;
import com.application.hotelbooking.dto.HotelDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelTransformer {
    @Autowired
    private ModelMapper modelMapper;

    public Hotel transformToHotel(HotelDTO hotelDTO){
        return modelMapper.map(hotelDTO, Hotel.class);
    }

    public HotelDTO transformToHotelDTO(Hotel hotel){
        return modelMapper.map(hotel, HotelDTO.class);
    }

    public List<HotelDTO> transformToHotelDTOs(List<Hotel> hotels){
        return hotels.stream()
                .map(hotel -> modelMapper.map(hotel, HotelDTO.class))
                .collect(Collectors.toList());
    }
}
