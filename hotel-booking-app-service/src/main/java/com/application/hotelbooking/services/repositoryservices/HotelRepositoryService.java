package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.dto.HotelDTO;
import com.application.hotelbooking.repositories.HotelRepository;
import com.application.hotelbooking.transformers.HotelTransformer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class HotelRepositoryService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelTransformer hotelTransformer;

    public HotelDTO getHotelDTO(Long hotelId){
        return hotelTransformer.transformToHotelDTO(hotelRepository.findById(hotelId).get());
    }

    public List<HotelDTO> getAllHotels(){
        return hotelTransformer.transformToHotelDTOs(hotelRepository.findAll());
    }

    public List<HotelDTO> findHotelByHotelName(String hotelName){
        return hotelTransformer.transformToHotelDTOs(hotelRepository.findHotelByHotelName(hotelName));
    }

    public HotelDTO save(HotelDTO hotelDTO){
        return hotelTransformer.transformToHotelDTO(hotelRepository.save(hotelTransformer.transformToHotel(hotelDTO)));
    }

}
