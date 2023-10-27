package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.domain.RoomType;
import com.application.hotelbooking.exceptions.InvalidRoomException;
import com.application.hotelbooking.repositories.RoomRepository;
import com.application.hotelbooking.transformers.RoomTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomService.class);
    public static final long DEFAULT_STARTING_VERSION = 1l;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTransformer roomTransformer;

    public List<RoomModel> getAllRooms(){
        return roomTransformer.transformToRoomModels(roomRepository.findAll());
    }

    public RoomModel getRoom(Long roomId){
        return roomTransformer.transformToRoomModel(roomRepository.findRoomById(roomId));
    }

    public List<RoomModel> findAllRoomsOfGivenType(String roomType){
        return roomTransformer.transformToRoomModels(roomRepository.findAllByRoomType(RoomType.valueOf(roomType)));
    }

    public boolean isRoomTypeNotAvailable(String roomType){
        return findAllRoomsOfGivenType(roomType).isEmpty();
    }

    public boolean isRoomNumberFree(int roomNumber){
        return roomRepository.findRoomByRoomNumber(roomNumber).size() == 0;
    }

    public void createRoom(RoomModel roomModel) throws InvalidRoomException{
        if (isRoomNumberFree(roomModel.getRoomNumber())) {
            roomModel.setVersion(DEFAULT_STARTING_VERSION);
            roomRepository.save(roomTransformer.transformToRoom(roomModel));
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
