package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.repositories.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomService.class);

    private static final List<String> ROOM_TYPES = List.of("familyRoom", "singleRoom");
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
//        // Logic should not depend on discriminator values.
//        String singleRoomType = SingleRoom.class.getAnnotation(jakarta.persistence.DiscriminatorValue.class).value();
//        LOGGER.info("The DiscriminatorValue of SingleRoom is: " + singleRoomType);

//        return ROOM_TYPES.contains(roomType);
        return findAllRoomsOfGivenType(roomType).isEmpty();
    }

}
