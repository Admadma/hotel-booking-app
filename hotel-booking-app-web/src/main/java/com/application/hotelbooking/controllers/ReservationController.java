package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.domain.ReservationView;
import com.application.hotelbooking.dto.DateRangeDto;
import com.application.hotelbooking.exceptions.InvalidTimePeriodException;
import com.application.hotelbooking.exceptions.InvalidUserException;
import com.application.hotelbooking.exceptions.NoRoomsAvailableException;
import com.application.hotelbooking.services.ReservationService;
import com.application.hotelbooking.services.UserService;
import com.application.hotelbooking.transformers.ReservationViewTransformer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Controller
@RequestMapping(path = "hotelbooking")
public class ReservationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private ReservationService reservationService;

    // TODO: remove this later. Controllers should not use more than one service
    @Autowired
    private UserService userService;

    @Autowired
    private ReservationViewTransformer reservationViewTransformer;

    @RequestMapping(value = "/select-date", headers = "Referer=")
    public String selectDate(@ModelAttribute("dateRange") DateRangeDto dateRangeDto, Authentication auth, HttpSession session){
        String roomType = session.getAttribute("roomType").toString();

        try {
            ReservationView reservation = reservationViewTransformer.transformToReservationView(
                    reservationService.prepareReservation(
                            roomType,
                            auth.getName(),
                            dateRangeDto.getStartDate(),
                            dateRangeDto.getEndDate()
                    )
            );
            LOGGER.info("Successfully prepared reservation: " + reservation.getId() + " reserved room: " + reservation.getRoom().getId() + " user: " + reservation.getUser().getUsername());
            session.setAttribute("reservation", reservation);
        } catch (InvalidTimePeriodException itp) {
            LOGGER.error("Invalid time period selected.");
            return "redirect:/hotelbooking/reservation?errorDate";
        }
        catch (NoRoomsAvailableException nae) {
            LOGGER.error("Could not make a reservation because there are no free rooms of that type");
            return "redirect:/hotelbooking/reservation?errorNoRooms";
        }

        return "redirect:/hotelbooking/confirmreservation";
    }

    // This will be useful for experimenting with admin user privileges
    @RequestMapping(value = "/clear-reservations")
    public String clearReservations(){
        reservationService.clearReservations();
        LOGGER.info("Cleared reservations");

        return "redirect:/hotelbooking/rooms";
    }

    @RequestMapping(value = "/delete-current-user")
    public String deleteCurrentUser(Authentication auth){
        userService.deleteUserByName(auth.getName());
        LOGGER.info("Deleted user");
        LOGGER.info("Name of deleted user: " + auth.getName());

        return "redirect:/hotelbooking/rooms";
    }

    @RequestMapping("/reservation")
    public String getRooms(Model model, HttpSession session){
        LOGGER.info("Navigating to reservation page");

        DateRangeDto dateRange = new DateRangeDto();
        model.addAttribute("dateRange", dateRange);
        model.addAttribute("roomType", session.getAttribute("roomType"));

        return "reservation";
    }
}
