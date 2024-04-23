package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.models.HotelModel;
import com.application.hotelbooking.dto.HotelCreationServiceDTO;

import java.util.List;
import java.util.Optional;

public interface HotelRepositoryService {
    Optional<HotelModel> getHotelById(Long hotelId);
    List<HotelModel> getAllHotels();
    Optional<HotelModel> findHotelByHotelName(String hotelName);
    HotelModel create(HotelCreationServiceDTO hotelCreationServiceDTO);
    HotelModel save(HotelModel hotelModel);
}
