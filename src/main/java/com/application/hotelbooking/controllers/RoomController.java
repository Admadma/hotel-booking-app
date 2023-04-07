package com.application.hotelbooking.controllers;

import com.application.hotelbooking.services.FamilyRoomService;
import com.application.hotelbooking.services.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "hotelbooking")
public class RoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomController.class);

//    @Autowired
//    private RoomService roomService;

    @Autowired
    @Qualifier("familyRoomService")
    private FamilyRoomService familyRoomService;

    @GetMapping("/rooms")
    public String getRooms(Model model){
        model.addAttribute("rooms", familyRoomService.getRooms());

        return "rooms";
    }
}
