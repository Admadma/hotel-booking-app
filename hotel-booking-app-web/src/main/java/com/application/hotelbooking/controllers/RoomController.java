package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.domain.RoomView;
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


    @RequestMapping(value = "/create-new-room")
    public String saveNewFamilyRoom(@Valid @ModelAttribute("roomView") RoomView roomView, BindingResult result){
        // TODO: room number unique validation before saving
        if (result.hasErrors()){
            LOGGER.info("Error while validating");
            LOGGER.info("Room number: " + roomView.getRoomNumber());
            LOGGER.info("Room id: " + roomView.getId());
            return "rooms";
        }

        LOGGER.info("Room number: " + roomView.getRoomNumber());
        LOGGER.info("Room type: " + roomView.getRoomType());
        LOGGER.info("Room id: " + roomView.getId());

        try {
            roomService.createRoom(roomViewTransformer.transformToRoomModel(roomView));
        } catch (Exception e) {
            LOGGER.error("Failed to save room: " + roomView);
//            result.addError(new ObjectError("globalError", "Failed to save room."));
            result.rejectValue("roomNumber", null, "That roomNumber is already taken");
            return "rooms";
        }
//        FamilyRoomView room = new FamilyRoomView();
//        room.setRoomNumber(206);
//        room.setDoubleBeds(1);
//        room.setSingleBeds(2);
//        roomService.createFamilyRoom(familyRoomViewTransformer.transformToFamilyRoomModel(room));
//        LOGGER.info("Created new FamilyRoom with id: " +  roomService.findAllRoomsOfGivenType("familyRoom").get(roomService.findAllRoomsOfGivenType("familyRoom").size()-1).getId());

        return "redirect:rooms";
    }


    @GetMapping("/rooms")
    public String getRooms(Model model){
        LOGGER.info("Navigating to rooms page");
        RoomView roomView = new RoomView();
        model.addAttribute("roomView", roomView);

        return "rooms";
    }
}
