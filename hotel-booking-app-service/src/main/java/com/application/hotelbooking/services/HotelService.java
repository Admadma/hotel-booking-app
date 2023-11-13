package com.application.hotelbooking.services;

import com.application.hotelbooking.dto.HotelCreationServiceDTO;

public interface HotelService {
    void createHotel(HotelCreationServiceDTO hotelCreationServiceDTO);
    int getLatestRoomNumberOfHotel(Long hotelId);
}
