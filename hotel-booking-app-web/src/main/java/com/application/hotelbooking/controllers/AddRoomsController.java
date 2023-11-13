package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.RoomCreationDTO;
import com.application.hotelbooking.exceptions.InvalidRoomException;
import com.application.hotelbooking.services.RoomService;
import com.application.hotelbooking.services.repositoryservices.HotelRepositoryService;
import com.application.hotelbooking.transformers.HotelViewTransformer;
import com.application.hotelbooking.transformers.RoomCreationDTOTransformer;
import com.application.hotelbooking.transformers.RoomViewTransformer;
import jakarta.servlet.http.HttpServletRequest;
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
    private HotelRepositoryService hotelRepositoryService;
    @Autowired
    private RoomViewTransformer roomViewTransformer;

    @Autowired
    private RoomCreationDTOTransformer roomCreationDTOTransformer;

    @Autowired
    private HotelViewTransformer hotelViewTransformer;

    @PostMapping(value = "/create-new-room")
    public String saveNewRoom(@Valid @ModelAttribute("roomCreationDTO") RoomCreationDTO roomCreationDTO, BindingResult result, Model model){
        if (result.hasErrors()){
            LOGGER.info("Error while validating");
            return "addrooms";
        }

        try {
            roomService.createRoomFromDTO(roomCreationDTOTransformer.transformToRoomCreationServiceDTO(roomCreationDTO));
            model.addAttribute("successMessage", "Success");
        } catch (Exception e){
            result.addError(new ObjectError("globalError", "Failed to save room"));
            return "addrooms";
        }
        return "addRooms";
    }

    @GetMapping("/addRooms")
    public String addRooms(Model model, HttpServletRequest request){
        LOGGER.info("Navigating to addRooms page");
        model.addAttribute("roomCreationDTO", new RoomCreationDTO());
        request.getSession().setAttribute("hotels", hotelViewTransformer.transformToHotelViews(hotelRepositoryService.getAllHotels()));

        return "addrooms";
    }
}
