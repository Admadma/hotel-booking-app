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

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTransformer roomTransformer;

    public List<RoomModel> getRooms(){
        return roomTransformer.transformToRoomModels(roomRepository.findAll());
    }

    public RoomModel getRoom(Long roomId){
        return roomTransformer.transformToRoomModel(roomRepository.findRoomById(roomId));
    }

    public List<RoomModel> findAllRoomsOfGivenType(String roomType){
        return roomTransformer.transformToRoomModels(roomRepository.findAllRoomsOfGivenType(roomType));
    }

    public boolean isRoomTypeNotAvailable(String roomType){
        return findAllRoomsOfGivenType(roomType).isEmpty();
    }

    public boolean roomNumberAlreadyExists(int roomNumber){
        if (roomRepository.findRoomByRoomNumber(roomNumber) == null){
            return false;
        }
        return true;
    }

    public void createRoom(RoomModel roomModel) throws InvalidRoomException{
        if (!roomNumberAlreadyExists(roomModel.getRoomNumber())) {
            roomRepository.save(roomTransformer.transformToRoom(roomModel));
        } else {
            throw new InvalidRoomException("That roomNumber is already taken.");
        }
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
