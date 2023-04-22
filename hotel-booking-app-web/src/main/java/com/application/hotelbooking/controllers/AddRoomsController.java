package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.RoomType;
import com.application.hotelbooking.domain.RoomView;
import com.application.hotelbooking.exceptions.InvalidRoomException;
import com.application.hotelbooking.services.RoomService;
import com.application.hotelbooking.transformers.RoomViewTransformer;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "hotelbooking/admin")
public class AddRoomsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddRoomsController.class);

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomViewTransformer roomViewTransformer;


    @RequestMapping(value = "/create-new-room")
    public String saveNewFamilyRoom(@Valid @ModelAttribute("roomView") RoomView roomView, BindingResult result){
        if (result.hasErrors()){
            result.addError(new ObjectError("globalError", "Failed to save room."));
//            System.out.println("RoomType: " + roomView.getRoomType());
//            result.getAllErrors().stream().forEach(System.out::println);
//            LOGGER.info("Error while validating");
//            LOGGER.info("Room number: " + roomView.getRoomNumber());
//            LOGGER.info("Room id: " + roomView.getId());
            return "addRooms";
        }

        LOGGER.info("Room number: " + roomView.getRoomNumber());
        LOGGER.info("Room type: " + roomView.getRoomType());
        LOGGER.info("Room id: " + roomView.getId());

        try {
            roomService.createRoom(roomViewTransformer.transformToRoomModel(roomView));
        } catch (InvalidRoomException ire) {
            LOGGER.error("Failed to save room: " + ire.getMessage());
            result.rejectValue("roomNumber", null, ire.getMessage());
        } catch (Exception e){
            result.addError(new ObjectError("globalError", "Failed to save room."));
            return "addRooms";
        }
        return  "addRooms";
    }


    @GetMapping("/addRooms")
    public String addRooms(Model model){
        LOGGER.info("Navigating to addRooms page");
        RoomView roomView = new RoomView();
        model.addAttribute("roomView", roomView);

        return "addrooms";
    }
}
