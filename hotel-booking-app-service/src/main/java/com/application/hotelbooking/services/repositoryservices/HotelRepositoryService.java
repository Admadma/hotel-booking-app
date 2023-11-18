package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.domain.HotelModel;
import com.application.hotelbooking.dto.HotelCreationServiceDTO;

import java.util.List;
import java.util.Optional;

public interface HotelRepositoryService {
    Optional<HotelModel> getHotelById(Long hotelId);
    List<HotelModel> getAllHotels();
    Optional<HotelModel> findHotelByHotelName(String hotelName);
    HotelModel save(HotelCreationServiceDTO hotelCreationServiceDTO);

    boolean testMethod();
}
