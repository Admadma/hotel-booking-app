package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.domain.RoomView;
import com.application.hotelbooking.exceptions.InvalidRoomException;
import com.application.hotelbooking.services.RoomService;
import com.application.hotelbooking.transformers.RoomViewTransformer;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

    @Autowired
    private RoomViewTransformer roomViewTransformer;


    // Receive request with room type -> store room type in session -> redirect to reservation page (should each room have their own description/reservation page or make one generic?)
    @RequestMapping(value = "/select-room-type")
    public String reserveRoom(@ModelAttribute("roomType") String roomType, BindingResult result, Authentication auth, HttpSession session){
        // Validate the received room type (users can edit html code and enter different strings)
//        roomType = "FAMILY_ROOM";
        if (roomService.isRoomTypeNotAvailable(roomType)){
            LOGGER.error("User attempted to select a room of invalid type: " + roomType);
            result.addError(new ObjectError("globalError", "There are currently no rooms of that type."));
            return "rooms";
        }
        session.setAttribute("roomType", roomType);

        return "redirect:reservation";
    }

    @GetMapping("/rooms")
    public String getRooms(){
        LOGGER.info("Navigating to rooms page");
        return "rooms";
    }
}
