package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.ReservationView;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping(path = "hotelbooking")
public class ConfirmReservationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmReservationController.class);

    @RequestMapping(value = "/confirm-reservation")
    public String reserve(@SessionAttribute("reservation") ReservationView reservationView, Authentication auth, HttpSession session){

        return "null";
    }


    @GetMapping("/confirmreservation")
    public String confirmReservation(@SessionAttribute("reservation") ReservationView reservationView, Model model){
        LOGGER.info("Navigating to confirmreservation page");

        model.addAttribute("reservation", reservationView);

        return "confirmreservation";
    }
}
