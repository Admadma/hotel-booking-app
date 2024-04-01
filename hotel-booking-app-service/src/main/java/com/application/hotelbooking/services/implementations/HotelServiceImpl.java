package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.domain.HotelModel;
import com.application.hotelbooking.domain.ReviewModel;
import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.dto.HotelCreationServiceDTO;
import com.application.hotelbooking.exceptions.InvalidHotelException;
import com.application.hotelbooking.services.HotelService;
import com.application.hotelbooking.services.repositoryservices.HotelRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelServiceImpl implements HotelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HotelService.class);

    @Autowired
    private HotelRepositoryService hotelRepositoryService;

    public HotelModel createHotel(HotelCreationServiceDTO hotelCreationServiceDTO) {
        if (isHotelNameFree(hotelCreationServiceDTO.getHotelName())){
            return hotelRepositoryService.create(hotelCreationServiceDTO);
        } else {
            throw new InvalidHotelException("That hotelName is already taken");
        }
    }

    private boolean isHotelNameFree(String hotelName) {
        return hotelRepositoryService.findHotelByHotelName(hotelName).isEmpty();
    }

    public int getLatestRoomNumberOfHotel(Long hotelId) {
        return hotelRepositoryService.getHotelById(hotelId).get()
                .getRooms()
                .stream()
                .mapToInt(RoomModel::getRoomNumber)
                .max()
                .orElse(0);
    }

    public void updateAverageRating(HotelModel hotelModel){
        double newRating = hotelModel.getReviews().stream().mapToDouble(ReviewModel::getRating).average().orElse(0);
        hotelModel.setAverageRating(newRating);
        hotelRepositoryService.save(hotelModel);
    }
}
