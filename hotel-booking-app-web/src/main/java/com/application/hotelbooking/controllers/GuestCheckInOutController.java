package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.ReservationView;
import com.application.hotelbooking.dto.HotelCreationDTO;
import com.application.hotelbooking.services.CheckInOutService;
import com.application.hotelbooking.transformers.ReservationViewTransformer;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping(path = "hotelbooking/admin/guest-check-in-out")
public class GuestCheckInOutController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestCheckInOutController.class);

    @Autowired
    private CheckInOutService checkInOutService;

    @Autowired
    private ReservationViewTransformer reservationViewTransformer;

    @GetMapping("/retrieve-reservation-info")
    public String retrieveReservationInfo(@RequestParam("reservationId") UUID uuid, HttpServletRequest request){
        LOGGER.info("Retrieving reservation info");

        ReservationView reservationView = reservationViewTransformer.transformToReservationView(checkInOutService.getReservationDetails(uuid));

        request.getSession().setAttribute("reservation", reservationView);

        return "guestcheckinout";
    }

    @PostMapping("/check-in")
    public String checkInGuest(@SessionAttribute("reservation") ReservationView reservationView, HttpServletRequest request){
        LOGGER.info("Checking in guest");

        try {
            checkInOutService.checkInGuest(reservationView.getUuid());
        } finally {
            request.getSession().removeAttribute("reservation");
        }

        return "guestcheckinout";
    }

    @PostMapping("/check-out")
    public String checkOutGuest(@SessionAttribute("reservation") ReservationView reservationView, HttpServletRequest request){
        LOGGER.info("Checking out guest");

        try {
            checkInOutService.checkOutGuest(reservationView.getUuid());
        } finally {
            request.getSession().removeAttribute("reservation");
        }

        return "guestcheckinout";
    }

    @GetMapping("")
    public String checkInOut(){
        LOGGER.info("Navigating to check in/out page");

        return "guestcheckinout";
    }
}
