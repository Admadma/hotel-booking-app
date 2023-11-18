package com.application.hotelbooking.services.repositoryservices.implementations;

import com.application.hotelbooking.domain.HotelModel;
import com.application.hotelbooking.dto.HotelCreationServiceDTO;
import com.application.hotelbooking.repositories.HotelRepository;
import com.application.hotelbooking.services.repositoryservices.HotelRepositoryService;
import com.application.hotelbooking.transformers.HotelTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelRepositoryServiceImpl implements HotelRepositoryService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelTransformer hotelTransformer;

    public Optional<HotelModel> getHotelById(Long hotelId){
        return hotelTransformer.transformToOptionalHotelModel(hotelRepository.findById(hotelId));
    }

    public List<HotelModel> getAllHotels(){
        return hotelTransformer.transformToHotelModels(hotelRepository.findAll());
    }

    public Optional<HotelModel> findHotelByHotelName(String hotelName){
        return hotelTransformer.transformToOptionalHotelModel(hotelRepository.findHotelByHotelName(hotelName));
    }

    public HotelModel save(HotelCreationServiceDTO hotelCreationServiceDTO){
        return hotelTransformer.transformToHotelModel(hotelRepository.save(hotelTransformer.transformToHotel(hotelCreationServiceDTO)));
    }
}
