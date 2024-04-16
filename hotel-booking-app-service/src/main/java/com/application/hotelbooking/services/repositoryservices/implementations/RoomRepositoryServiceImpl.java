package com.application.hotelbooking.services.repositoryservices.implementations;

import com.application.hotelbooking.domain.RoomType;
import com.application.hotelbooking.dto.RoomCreationServiceDTO;
import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.dto.RoomSearchFormServiceDTO;
import com.application.hotelbooking.repositories.RoomRepository;
import com.application.hotelbooking.services.repositoryservices.RoomRepositoryService;
import com.application.hotelbooking.transformers.RoomTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomRepositoryServiceImpl implements RoomRepositoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomRepositoryService.class);

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTransformer roomTransformer;

    public Optional<RoomModel> getRoomById(Long roomId){
        return roomTransformer.transformToOptionalRoomModel(roomRepository.findById(roomId));
    }

    public Optional<RoomModel> findRoomByNumberAndHotelName(int roomNumber, String hotelName){
        return roomTransformer.transformToOptionalRoomModel(roomRepository.findRoomByRoomNumberAndHotelHotelName(roomNumber, hotelName));
    }

    public RoomModel saveRoom(RoomCreationServiceDTO roomCreationServiceDTO){
        return roomTransformer.transformToRoomModel(roomRepository.save(roomTransformer.transformToRoom(roomCreationServiceDTO)));
    }

    public void incrementRoomVersion(RoomModel roomModel){
        roomModel.setVersion(roomModel.getVersion() + 1);
        roomRepository.save(roomTransformer.transformToRoom(roomModel));
    }

    public List<Long> getRoomsWithConditions(RoomSearchFormServiceDTO roomSearchFormServiceDTO) {
        return roomRepository.findRoomsWithConditions(roomSearchFormServiceDTO.getSingleBeds(),
                roomSearchFormServiceDTO.getDoubleBeds(),
                roomSearchFormServiceDTO.getRoomType(),
                roomSearchFormServiceDTO.getHotelName(),
                roomSearchFormServiceDTO.getCity());
    }

    public List<Long> getRoomsWithConditions(int singleBeds, int doubleBeds, RoomType roomType, String hotelName, String city) {
        return roomRepository.findRoomsWithConditions(singleBeds,
                doubleBeds,
                roomType,
                hotelName,
                city);
    }
}

