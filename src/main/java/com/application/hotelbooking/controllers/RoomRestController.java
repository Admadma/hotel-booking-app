package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.services.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoomRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomRestController.class);

    @Autowired
    private RoomService roomService;

    @GetMapping("/rooms_rest")
    public List<Room> getRooms(){
        return roomService.getRooms();
    }


}
