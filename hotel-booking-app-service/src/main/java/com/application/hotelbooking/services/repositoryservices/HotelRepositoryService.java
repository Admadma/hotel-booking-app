package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.domain.HotelModel;
import com.application.hotelbooking.repositories.HotelRepository;
import com.application.hotelbooking.transformers.HotelTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelRepositoryService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelTransformer hotelTransformer;

    public HotelModel getHotelDTO(Long hotelId){
        return hotelTransformer.transformToHotelModel(hotelRepository.findById(hotelId).get());
    }

    public List<HotelModel> getAllHotels(){
        return hotelTransformer.transformToHotelModels(hotelRepository.findAll());
    }

    public List<HotelModel> findHotelByHotelName(String hotelName){
        return hotelTransformer.transformToHotelModels(hotelRepository.findHotelByHotelName(hotelName));
    }

    public HotelModel save(HotelModel hotelModel){
        return hotelTransformer.transformToHotelModel(hotelRepository.save(hotelTransformer.transformToHotel(hotelModel)));
    }

}
