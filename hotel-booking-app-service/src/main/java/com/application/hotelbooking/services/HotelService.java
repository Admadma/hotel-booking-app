package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.HotelModel;
import com.application.hotelbooking.repositories.HotelRepository;
import com.application.hotelbooking.transformers.HotelTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HotelService.class);
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelTransformer hotelTransformer;


    public void createHotel(HotelModel hotelModel){
        hotelRepository.save(hotelTransformer.transformToHotel(hotelModel));

    }
}
