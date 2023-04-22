package com.application.hotelbooking.controllers;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "hotelbooking")
public class AddRoomsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddRoomsController.class);



    @GetMapping("/addRooms")
    public String addRooms(){
        LOGGER.info("Navigating to addrooms page");

        return "addrooms";
    }
}
