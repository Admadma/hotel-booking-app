package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.RoomSearchFormDTO;
import com.application.hotelbooking.services.RoomService;
import com.application.hotelbooking.transformers.RoomSearchFormDTOTransformer;
import com.application.hotelbooking.transformers.RoomViewTransformer;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping(path = "hotelbooking")
public class HomeController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomViewTransformer roomViewTransformer;

    @Autowired
    private RoomSearchFormDTOTransformer roomSearchFormDTOTransformer;

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    private void transformFieldsToNulls(RoomSearchFormDTO roomSearchFormDTO){
        if ("".equals(roomSearchFormDTO.getCity())){
            roomSearchFormDTO.setCity(null);
        }
        if ("".equals(roomSearchFormDTO.getRoomType())){
            roomSearchFormDTO.setRoomType(null);
        }
    }

    @PostMapping(value = "/search-rooms")
    public String searchRooms(@Valid @ModelAttribute("roomSearchFormDTO") RoomSearchFormDTO roomSearchFormDTO, BindingResult result){
        if (result.hasErrors()){
            LOGGER.info("Error while validating");
            return "homepage";
        }
        transformFieldsToNulls(roomSearchFormDTO);
        roomService.searchRooms(roomSearchFormDTOTransformer.transformToRoomSearchFormServiceDTO(roomSearchFormDTO));

//        RoomService.listRooms(roomSearchFormDTO)

        LOGGER.info("No Error");
        return "homepage";
    }


    @GetMapping("/home")
    public String home(Model model){
        LOGGER.info("Navigating to home page");
        model.addAttribute("hotels", roomViewTransformer.transformToRoomViews(roomService.getAllRooms()));
        model.addAttribute("roomSearchFormDTO", new RoomSearchFormDTO());

        return "homepage";
    }
}
