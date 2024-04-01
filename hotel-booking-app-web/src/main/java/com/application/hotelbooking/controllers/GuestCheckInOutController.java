package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.HotelCreationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "hotelbooking/admin")
public class GuestCheckInOutController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestCheckInOutController.class);

    @GetMapping("/checkInOut")
    public String checkInOut(Model model, @ModelAttribute("fileUploadError") String fileUploadError){
        LOGGER.info("Navigating to check in/out page");

        if(!fileUploadError.isBlank()) {
            model.addAttribute("error", fileUploadError);
        }
        model.addAttribute("hotelCreationDTO", new HotelCreationDTO());
        return "guestcheckinout";
    }
}
