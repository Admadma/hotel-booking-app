package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.RoomSearchFormDTO;
import com.application.hotelbooking.services.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReserveRoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReserveRoomController.class);

    @Autowired
    private RoomService roomService;

    @GetMapping("/reserveroom")
    public String reserveRoom(Model model){
        LOGGER.info("Navigating to reserveroom page");
//        model.addAttribute("roomSearchFormDTO", new RoomSearchFormDTO());

        return "reserveroom";
    }
}
