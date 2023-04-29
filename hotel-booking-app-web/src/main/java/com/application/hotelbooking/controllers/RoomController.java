package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.RoomType;
import com.application.hotelbooking.services.RoomService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "hotelbooking")
public class RoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private RoomService roomService;

    @RequestMapping(value = "/select-room-type")
    public String reserveRoom(@ModelAttribute("roomType") String roomType, BindingResult result, HttpSession session){
        if (roomService.isRoomTypeNotAvailable(roomType)){
            LOGGER.error("User attempted to select a room of invalid type: " + roomType);
            result.addError(new ObjectError("globalError", "There are currently no rooms of that type."));
            return "rooms";
        }
        session.setAttribute("roomType", RoomType.valueOf(roomType));
//        session.setAttribute("selectedRoom", roomService.find(roomType));

        return "redirect:reservation";
    }

    @GetMapping("/rooms")
    public String getRooms(){
        LOGGER.info("Navigating to rooms page");
        return "rooms";
    }
}
