package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.ReservationView;
import com.application.hotelbooking.dto.ChangeCredentialsDto;
import com.application.hotelbooking.exceptions.CancellationErrorException;
import com.application.hotelbooking.exceptions.InvalidUserException;
import com.application.hotelbooking.services.ReservationService;
import com.application.hotelbooking.services.UserService;
import com.application.hotelbooking.transformers.ReservationViewTransformer;
import com.application.hotelbooking.transformers.UserViewTransformer;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "hotelbooking")
public class MyReservationsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyReservationsController.class);

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationViewTransformer reservationViewTransformer;
    @Autowired
    private UserService userService;

    @Autowired
    private UserViewTransformer userViewTransformer;

    @RequestMapping(value = "/cancel-reservation")
    public String cancelReservation(@ModelAttribute("reservationId") Long reservationId, Authentication auth){
        try {
            reservationService.deleteReservationOfUser(auth.getName(), reservationId);
        } catch (CancellationErrorException cee) {
            LOGGER.info(cee.getMessage());
            return "redirect:/hotelbooking/myreservations?error";
        }
        LOGGER.info("Successfully deleted reservation!");

        return "redirect:/hotelbooking/myreservations?success";
    }


    @GetMapping("/myreservations")
    public String myReservations(Authentication auth, Model model){
        LOGGER.info("Navigating to myreservations page");
        List<ReservationView> reservations = reservationViewTransformer.transformToReservationViews(reservationService.getReservationsOfUser(auth.getName()));
        model.addAttribute("reservations", reservations);
        return "myreservations";
    }
}