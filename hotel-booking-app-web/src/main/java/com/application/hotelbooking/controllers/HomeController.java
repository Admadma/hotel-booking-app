package com.application.hotelbooking.controllers;

import com.application.hotelbooking.services.RoomService;
import com.application.hotelbooking.transformers.RoomViewTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "hotelbooking")
public class HomeController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomViewTransformer roomViewTransformer;

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/home")
    public String home(Model model){
        LOGGER.info("Navigating to home page");
        model.addAttribute("hotels", roomViewTransformer.transformToRoomViews(roomService.getAllRooms()));

        return "homepage";
    }
}
