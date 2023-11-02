package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.HotelModel;
import com.application.hotelbooking.dto.HotelDTO;
import com.application.hotelbooking.exceptions.InvalidHotelException;
import com.application.hotelbooking.repositories.HotelRepository;
import com.application.hotelbooking.transformers.HotelTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HotelService.class);
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelTransformer hotelTransformer;

    public HotelDTO getHotelDTO(Long hotelId){
        return hotelTransformer.transformToHotelDTO(hotelRepository.findById(hotelId).get());
    }

    public List<HotelModel> getAllHotels(){
        return hotelTransformer.transformToHotelModels(hotelRepository.findAll());
    }


    public void createHotel(HotelModel hotelModel){
        if (isHotelNameFree(hotelModel)){
            hotelRepository.save(hotelTransformer.transformToHotel(hotelModel));
        } else {
            throw new InvalidHotelException("That hotelName is already taken");
        }
    }

    private boolean isHotelNameFree(HotelModel hotelModel) {
        return hotelRepository.findHotelByHotelName(hotelModel.getHotelName()).size() == 0;
    }
}
