package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.dto.RoomCreationServiceDTO;
import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.dto.RoomSearchFormServiceDTO;
import com.application.hotelbooking.repositories.RoomRepository;
import com.application.hotelbooking.transformers.RoomTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomRepositoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomRepositoryService.class);

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTransformer roomTransformer;

    public RoomModel getRoomDTO(Long roomId){
        return roomTransformer.transformToRoomModel(roomRepository.findById(roomId).get());
    }

    public RoomModel findRoomByNumberAndHotelName(int roomNumber, String hotelName){
        return roomTransformer.transformToRoomModel(roomRepository.findRoomByRoomNumberAndHotelHotelName(roomNumber, hotelName));
    }

    public RoomModel saveRoom(RoomCreationServiceDTO roomCreationServiceDTO){
        return roomTransformer.transformToRoomModel(roomRepository.save(roomTransformer.transformToRoom(roomCreationServiceDTO)));
    }

    public RoomModel updateRoom(RoomModel roomModel){
        return roomTransformer.transformToRoomModel(roomRepository.save(roomTransformer.transformToRoom(roomModel)));
    }

    public List<Long> getRoomsWithConditions(RoomSearchFormServiceDTO roomSearchFormServiceDTO) {
        return roomRepository.findRoomsWithConditions(roomSearchFormServiceDTO.getSingleBeds(),
                roomSearchFormServiceDTO.getDoubleBeds(),
                roomSearchFormServiceDTO.getRoomType(),
                roomSearchFormServiceDTO.getHotelName(),
                roomSearchFormServiceDTO.getCity());
    }
}

