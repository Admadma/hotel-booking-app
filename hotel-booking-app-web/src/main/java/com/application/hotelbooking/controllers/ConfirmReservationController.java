package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.ReservationView;
import com.application.hotelbooking.services.ReservationService;
import com.application.hotelbooking.transformers.ReservationViewTransformer;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping(path = "hotelbooking")
public class ConfirmReservationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmReservationController.class);

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationViewTransformer reservationViewTransformer;

    @RequestMapping(value = "/confirm-reservation")
    public String reserveRoom(@SessionAttribute("reservation") ReservationView reservationView, Authentication auth, HttpSession session){

        try {
            reservationService.reserve(reservationViewTransformer.transformToReservationModel(reservationView));
        } catch (OptimisticLockException ole){
            LOGGER.error("Failed to reserve room, because someone else already made a reservation with that same room.");
            return "redirect:/hotelbooking/confirmreservation?errorRoomTaken";
        }
        LOGGER.info("Successfully reserved the room!");

        return "redirect:/hotelbooking/home";
    }


    @GetMapping("/confirmreservation")
    public String confirmReservation(){
        LOGGER.info("Navigating to confirmreservation page");

        return "confirmreservation";
    }
}
