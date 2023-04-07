package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.repositories.RoomBaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("singleRoomService")
public class SingleRoomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomService.class);
    @Autowired
    @Qualifier("singleRoomRepository")
    private RoomBaseRepository roomRepository;

    public List<Room> getRooms(){
        return roomRepository.findAll();
    }

    public Room getRoom(Long roomId){
        return roomRepository.findRoomById(roomId);
    }
}
