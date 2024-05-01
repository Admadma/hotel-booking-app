package com.application.hotelbooking.services;

import com.application.hotelbooking.models.HotelModel;
import com.application.hotelbooking.dto.HotelCreationServiceDTO;

public interface HotelService {
    HotelModel createHotel(HotelCreationServiceDTO hotelCreationServiceDTO);
    int getLatestRoomNumberOfHotel(Long hotelId);
    void updateAverageRating(HotelModel hotelModel);
}
