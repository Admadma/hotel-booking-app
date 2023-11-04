package com.application.hotelbooking.services;

import com.application.hotelbooking.dto.HotelDTO;
import com.application.hotelbooking.exceptions.InvalidHotelException;
import com.application.hotelbooking.services.repositoryservices.HotelRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HotelService.class);

    @Autowired
    private HotelRepositoryService hotelRepositoryService;

    public void createHotel(HotelDTO hotelDTO){
        if (isHotelNameFree(hotelDTO)){
            hotelRepositoryService.save(hotelDTO);
        } else {
            throw new InvalidHotelException("That hotelName is already taken");
        }
    }

    private boolean isHotelNameFree(HotelDTO hotelDTO) {
        return hotelRepositoryService.findHotelByHotelName(hotelDTO.getHotelName()).size() == 0;
    }
}
