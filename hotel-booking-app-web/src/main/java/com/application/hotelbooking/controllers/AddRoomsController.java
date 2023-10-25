package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.RoomView;
import com.application.hotelbooking.exceptions.InvalidRoomException;
import com.application.hotelbooking.services.RoomService;
import com.application.hotelbooking.transformers.RoomViewTransformer;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "hotelbooking/admin")
public class AddRoomsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddRoomsController.class);

    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomViewTransformer roomViewTransformer;

    @PostMapping(value = "/create-new-room")
    public String saveNewRoom(@Valid @ModelAttribute("roomView") RoomView roomView, BindingResult result, Model model){
        if (result.hasErrors()){
            LOGGER.info("Error while validating");
            return "addrooms";
        }

        try {
            roomService.createRoom(roomViewTransformer.transformToRoomModel(roomView));
            model.addAttribute("successMessage", "Success");
        } catch (InvalidRoomException ire) {
            LOGGER.error("Failed to save room: " + ire.getMessage());
            result.rejectValue("roomNumber", "admin.room.validation.roomnumber.taken");
        } catch (Exception e){
            result.addError(new ObjectError("globalError", "Failed to save room"));
            return "addrooms";
        }
        return "addRooms";
    }

    @GetMapping("/addRooms")
    public String addRooms(Model model){
        LOGGER.info("Navigating to addRooms page");
        model.addAttribute("roomView", new RoomView());

        return "addrooms";
    }
}
