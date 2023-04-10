package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.FamilyRoom;
import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.repositories.RoomBaseRepository;
import com.application.hotelbooking.repositories.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
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

}
