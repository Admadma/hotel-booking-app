package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.domain.rooms.FamilyRoomView;
import com.application.hotelbooking.services.RoomService;
import com.application.hotelbooking.transformers.room.FamilyRoomViewTransformer;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    private FamilyRoomViewTransformer familyRoomViewTransformer;


    // Receive request with room type -> store room type in session -> redirect to reservation page (should each room have their own description/reservation page or make one generic?)
    @RequestMapping(value = "/select-room-type")
    public String reserveRoom(@ModelAttribute("roomType") String roomType, BindingResult result, Authentication auth, HttpSession session){
        // Validate the received room type (users can edit html code and enter different strings)
        if (roomService.isRoomTypeNotAvailable(roomType)){
            LOGGER.error("User attempted to select a room of invalid type: " + roomType);
            result.addError(new ObjectError("globalError", "There are no rooms of that type."));
            return "rooms";
        }
        session.setAttribute("roomType", roomType);

        return "redirect:reservation";
    }


    @RequestMapping(value = "/save-new-family-room")
    public String saveNewFamilyRoom(){
        // TODO: room number unique validation before saving
        FamilyRoomView room = new FamilyRoomView();
        room.setRoomNumber(206);
        room.setDoubleBeds(1);
        room.setSingleBeds(2);
        roomService.createFamilyRoom(familyRoomViewTransformer.transformToFamilyRoomModel(room));
        LOGGER.info("Created new FamilyRoom with id: " +  roomService.findAllRoomsOfGivenType("familyRoom").get(roomService.findAllRoomsOfGivenType("familyRoom").size()-1).getId());

        return "redirect:rooms";
    }


    @GetMapping("/rooms")
    public String getRooms(){
        LOGGER.info("Navigating to rooms page");
        return "rooms";
    }
}
