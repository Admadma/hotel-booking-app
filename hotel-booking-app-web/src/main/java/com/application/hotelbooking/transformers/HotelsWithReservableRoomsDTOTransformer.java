package com.application.hotelbooking.transformers;

import com.application.hotelbooking.dto.HotelWithReservableRoomsDTO;
import com.application.hotelbooking.dto.HotelWithReservableRoomsServiceDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelsWithReservableRoomsDTOTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public List<HotelWithReservableRoomsDTO> transformToHotelsWithReservableRoomsDTOs(List<HotelWithReservableRoomsServiceDTO> hotelWithReservableRoomsServiceDTOS){
        return hotelWithReservableRoomsServiceDTOS.stream()
                .map(hotelWithReservableRoomsServiceDTO -> modelMapper.map(hotelWithReservableRoomsServiceDTO, HotelWithReservableRoomsDTO.class))
                .collect(Collectors.toList());
    }

    public List<HotelWithReservableRoomsServiceDTO> transformToHotelWithReservableRoomsServiceDTOs(List<HotelWithReservableRoomsDTO> hotelWithReservableRoomsDTOS){
        modelMapper.typeMap(HotelWithReservableRoomsDTO.class, HotelWithReservableRoomsServiceDTO.class).addMappings(modelMapper -> modelMapper.map(HotelWithReservableRoomsDTO::getUniqueReservableRoomOfHotelDTOList, HotelWithReservableRoomsServiceDTO::setUniqueReservableRoomOfHotelServiceDTOList));
        return hotelWithReservableRoomsDTOS.stream()
                .map(hotelWithReservableRoomsDTO -> modelMapper.map(hotelWithReservableRoomsDTO, HotelWithReservableRoomsServiceDTO.class))
                .collect(Collectors.toList());
    }
}
