package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.HotelModel;
import com.application.hotelbooking.dto.HotelCreationServiceDTO;
import com.application.hotelbooking.exceptions.InvalidHotelException;

public interface HotelService {
    HotelModel createHotel(HotelCreationServiceDTO hotelCreationServiceDTO);
    int getLatestRoomNumberOfHotel(Long hotelId);
}
