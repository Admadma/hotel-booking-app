package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.ReservationView;
import com.application.hotelbooking.dto.HotelCreationDTO;
import com.application.hotelbooking.services.CheckInOutService;
import com.application.hotelbooking.transformers.ReservationViewTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping(path = "hotelbooking/admin")
public class GuestCheckInOutController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestCheckInOutController.class);

    @Autowired
    private CheckInOutService checkInOutService;

    @Autowired
    private ReservationViewTransformer reservationViewTransformer;

    @GetMapping(value = "/retrieve-reservation-info")
    public String saveNewHotel(@RequestParam("reservationId") UUID uuid, Model model){
        LOGGER.info("here");
        LOGGER.info(uuid.toString());

        ReservationView reservationView = reservationViewTransformer.transformToReservationView(checkInOutService.getReservationDetails(uuid));

        model.addAttribute("reservation", reservationView);

        return "guestcheckinout";
    }

    @GetMapping("/checkInOut")
    public String checkInOut(Model model){
        LOGGER.info("Navigating to check in/out page");

        model.addAttribute("hotelCreationDTO", new HotelCreationDTO());
        return "guestcheckinout";
    }
}
