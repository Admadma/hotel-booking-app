package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.domain.RoomType;
import com.application.hotelbooking.repositories.RoomRepository;
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

    public List<Room> getRooms(){
        return roomRepository.findAll();
    }

    public Room getRoom(Long roomId){
        return roomRepository.findRoomById(roomId);
    }

    public List<Room> findAllRoomsOfGivenType(String roomType){
        return roomRepository.findAllRoomsOfGivenType(roomType);
    }

    public boolean isRoomTypeNotAvailable(String roomType){
        return findAllRoomsOfGivenType(roomType).isEmpty();
    }

    public void createRoom(){
        Room room = new Room();
        room.setRoomNumber(204);
        room.setDoubleBeds(1);
        room.setSingleBeds(2);
        room.setRoomType(RoomType.FAMILY_ROOM);
        roomRepository.save(room);
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
