package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.domain.RoomType;
import com.application.hotelbooking.dto.RoomCreationServiceDTO;
import com.application.hotelbooking.dto.RoomSearchFormServiceDTO;
import com.application.hotelbooking.dto.RoomSearchResultDTO;
import com.application.hotelbooking.exceptions.InvalidRoomException;
import com.application.hotelbooking.repositories.RoomRepository;
import com.application.hotelbooking.transformers.RoomCreationServiceDTOTransformer;
import com.application.hotelbooking.transformers.RoomTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RoomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomService.class);
    public static final long DEFAULT_STARTING_VERSION = 1l;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTransformer roomTransformer;

    @Autowired
    private RoomCreationServiceDTOTransformer roomCreationServiceDTOTransformer;

    public List<RoomModel> getAllRooms(){
        return roomTransformer.transformToRoomModels(roomRepository.findAll());
    }

    public RoomModel getRoom(Long roomId){
        return roomTransformer.transformToRoomModel(roomRepository.findRoomById(roomId));
    }

    public List<RoomModel> findAllRoomsOfGivenType(String roomType){
        return roomTransformer.transformToRoomModels(roomRepository.findAllByRoomType(RoomType.valueOf(roomType)));
    }

    public List<RoomSearchResultDTO> searchRooms(RoomSearchFormServiceDTO roomSearchFormServiceDTO){
        roomRepository.findRoomsWithConditions(roomSearchFormServiceDTO.getSingleBeds(),
                roomSearchFormServiceDTO.getDoubleBeds(),
                roomSearchFormServiceDTO.getRoomType(),
                roomSearchFormServiceDTO.getHotelName(),
                roomSearchFormServiceDTO.getCity())
                .stream()
                .forEach(room -> LOGGER.info("Room number: " + room.getRoomNumber() +
                    " singleBeds: " + room.getSingleBeds()  +
                    " doubleBeds: " + room.getDoubleBeds() +
                    " roomType: " + room.getRoomType() +
                    " city: " + room.getHotel().getCity() +
                    " hotel: " + room.getHotel().getHotelName()
                ));
        LOGGER.info("---------");

        // Now filter for which room can be reserved in given time period
        return null;
    }

    public boolean isRoomTypeNotAvailable(String roomType){
        return findAllRoomsOfGivenType(roomType).isEmpty();
    }

    public boolean isRoomNumberFree(int roomNumber){
        return roomRepository.findRoomByRoomNumber(roomNumber).size() == 0;
    }

    public void createRoomFromDTO(RoomCreationServiceDTO roomCreationServiceDTO){
        if (isRoomNumberFree(roomCreationServiceDTO.getRoomNumber())) {
            roomCreationServiceDTO.setVersion(DEFAULT_STARTING_VERSION);
            roomRepository.save(roomCreationServiceDTOTransformer.transformToRoom(roomCreationServiceDTO));
        } else {
            throw new InvalidRoomException("That roomNumber is already taken.");
        }
    }

    public void updateRoomVersion(RoomModel roomModel){
        roomModel.setVersion(roomModel.getVersion() + 1);
        roomRepository.save(roomTransformer.transformToRoom(roomModel));
    }

    public boolean roomVersionMatches(RoomModel roomModel) {
        return getRoom(roomModel.getId()).getVersion().equals(roomModel.getVersion());
    }

//    private Room roomFactoryCreate(String roomType,){
//        Room room;
//        if (roomType == "familyRoom"){
//            room = new FamilyRoom();
//            room.set
//        }
//    }

//    public void createFamilyRoom(int roomNumber, int singleBeds, int doubleBeds) {
//    public void createFamilyRoom(FamilyRoomModel familyRoomModel) {
//        roomRepository.save(familyRoomTransformer.transformToFamilyRoom(familyRoomModel));
//    }
}
