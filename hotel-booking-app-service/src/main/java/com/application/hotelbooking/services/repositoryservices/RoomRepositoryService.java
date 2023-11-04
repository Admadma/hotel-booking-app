package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.dto.RoomCreationServiceDTO;
import com.application.hotelbooking.dto.RoomDTO;
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

    public RoomDTO getRoomDTO(Long roomId){
        return roomTransformer.transformToRoomDTO(roomRepository.findById(roomId).get());
    }

    public RoomDTO findRoomByNumberAndHotelName(int roomNumber, String hotelName){
        return roomTransformer.transformToRoomDTO(roomRepository.findRoomByRoomNumberAndHotelHotelName(roomNumber, hotelName));
    }

    public RoomDTO saveRoom(RoomCreationServiceDTO roomCreationServiceDTO){
        return roomTransformer.transformToRoomDTO(roomRepository.save(roomTransformer.transformToRoom(roomCreationServiceDTO)));
    }

    public boolean isRoomNumberFree(int roomNumber){
        return roomRepository.findRoomByRoomNumber(roomNumber).size() == 0;
    }

    public List<Long> getRoomsWithConditions(RoomSearchFormServiceDTO roomSearchFormServiceDTO) {
        return roomRepository.findRoomsWithConditions(roomSearchFormServiceDTO.getSingleBeds(),
                roomSearchFormServiceDTO.getDoubleBeds(),
                roomSearchFormServiceDTO.getRoomType(),
                roomSearchFormServiceDTO.getHotelName(),
                roomSearchFormServiceDTO.getCity());
    }
}

