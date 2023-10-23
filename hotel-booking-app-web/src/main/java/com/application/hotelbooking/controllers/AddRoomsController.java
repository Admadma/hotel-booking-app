package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.HotelView;
import com.application.hotelbooking.domain.RoomType;
import com.application.hotelbooking.domain.RoomView;
import com.application.hotelbooking.exceptions.InvalidRoomException;
import com.application.hotelbooking.services.HotelService;
import com.application.hotelbooking.services.RoomService;
import com.application.hotelbooking.transformers.HotelViewTransformer;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "hotelbooking/admin")
public class AddRoomsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddRoomsController.class);

    @Autowired
    private RoomService roomService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomViewTransformer roomViewTransformer;

    @Autowired
    private HotelViewTransformer hotelViewTransformer;


    @PostMapping(value = "/create-new-room")
    public String saveNewRoom(@Valid @ModelAttribute("roomView") RoomView roomView, BindingResult result){
        if (result.hasErrors()){
            result.addError(new ObjectError("globalError", "Failed to save room."));
////            System.out.println("RoomType: " + roomView.getRoomType());
//            result.getAllErrors().stream().forEach(System.out::println);
//
//            LOGGER.info("fieldError: " + result.getFieldError());
//            LOGGER.info("fieldError: " + result.getFieldError().getField());
//            LOGGER.info("fieldError: " + result.getFieldError().getRejectedValue());
//            LOGGER.info("fieldError size: " + result.getFieldErrors().size());
//            result.getFieldErrors().stream().forEach(fieldError -> System.out.println(fieldError.getField()));
//            LOGGER.info("Error while validating");
////            result.rejectValue("roomType", null, "default message");
////            result.rejectValue("roomNumber", null, "default message");
////            LOGGER.info("Room number: " + roomView.getRoomNumber());
////            LOGGER.info("Room id: " + roomView.getId());
//            LOGGER.info("-------");
            return "addrooms";
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
            return "addrooms";
        }
        return "addRooms";
    }

    @PostMapping(value = "/create-new-hotel")
    public String saveNewHotel(@Valid @ModelAttribute("hotelView") HotelView hotelView, BindingResult result){
        LOGGER.info("called create-new-hotel");
        if (result.hasErrors()){
            result.addError(new ObjectError("globalError", "Failed to save hotel."));
//            System.out.println("RoomType: " + roomView.getRoomType());
//            result.getAllErrors().stream().forEach(System.out::println);
            LOGGER.info("Error while validating");
//            LOGGER.info("Room number: " + roomView.getRoomNumber());
//            LOGGER.info("Room id: " + roomView.getId());
            return "addrooms";
        }

        try {
            hotelService.createHotel(hotelViewTransformer.transformToRoomModel(hotelView));
        } catch (Exception e){
            result.addError(new ObjectError("globalError", "Failed to save hotel"));
            return "addrooms";
        }
        return "addRooms";
    }

    @GetMapping("/addRooms")
    public String addRooms(Model model){
        LOGGER.info("Navigating to addRooms page");
        model.addAttribute("roomView", new RoomView());
        model.addAttribute("hotelView", new HotelView());

        return "addrooms";
    }
}
