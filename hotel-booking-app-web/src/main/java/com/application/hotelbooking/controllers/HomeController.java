package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.UserDto;
import com.application.hotelbooking.repositories.RoleRepository;
import com.application.hotelbooking.repositories.UserRepository;
import com.application.hotelbooking.services.HotelService;
import com.application.hotelbooking.transformers.HotelTransformer;
import com.application.hotelbooking.transformers.HotelViewTransformer;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(path = "hotelbooking")
public class HomeController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private HotelViewTransformer hotelViewTransformer ;

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/home")
    public String home(Model model){
        LOGGER.info("Navigating to home page");
        model.addAttribute("hotels", hotelViewTransformer.transformToHotelViews(hotelService.getAllHotels()));

        return "homepage";
    }
}
