package com.application.hotelbooking.controllers;

import com.application.hotelbooking.services.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "hotelbooking")
public class RoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomController.class);

    @GetMapping("/rooms")
    public String getRooms(){
        LOGGER.info("Navigating to rooms page");
        return "rooms";
    }
}
