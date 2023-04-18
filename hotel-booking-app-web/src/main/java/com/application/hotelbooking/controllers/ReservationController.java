package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.dto.DateRangeDto;
import com.application.hotelbooking.exceptions.InvalidTimePeriodException;
import com.application.hotelbooking.exceptions.InvalidUserException;
import com.application.hotelbooking.exceptions.NoRoomsAvailableException;
import com.application.hotelbooking.services.ReservationService;
import com.application.hotelbooking.services.UserService;
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

    @RequestMapping(value = "/reservation/select-date")
    public String reserveRoom(@ModelAttribute("dateRange") DateRangeDto dateRangeDto, Authentication auth, HttpSession session){
        // TODO: later surround this with a try-catch. Exceptions can be for example: time period is invalid
        // TODO: user can edit the html code and change the room id. Code needs to check that there was no tampering with the parameters (e.g. clicking book this room but the id was changed to a different room)

        // auth -> some_validator -> checks whether there is exactly one instance of that user in the database. -> if false -> invalidate http and redirect to login page
        String roomType = session.getAttribute("roomType").toString();
        LOGGER.info("Start date: " + dateRangeDto.getStartDate());
        LOGGER.info("End date: " + dateRangeDto.getEndDate());
        LOGGER.debug("room type: " + roomType);
        try {
            Reservation reservation =reservationService.reserve(
                    roomType,
                    auth.getName(),
                    dateRangeDto.getStartDate(),
                    dateRangeDto.getEndDate()
            );
            LOGGER.info("Successfully created reservation: " + reservation.getId() + " reserved room: " + reservation.getRoom().getId() + " user: " + reservation.getUser().getUsername());
        } catch (InvalidUserException iue){
            LOGGER.error(iue.getMessage());
            session.invalidate();
            // After invalidating, I don't want to reach the rest of this method
            return "redirect:/login";
        } catch (InvalidTimePeriodException itp) {
            LOGGER.error("Invalid time period selected.");
            return "redirect:/hotelbooking/reservation";
        }
        catch (NoRoomsAvailableException nae) {
            LOGGER.error("Could not make a reservation because there are no free rooms of that type");
        }

        LOGGER.info("Id of rooms that have a reservation:");
        for (Reservation reservation: reservationService.getReservations()) {
            LOGGER.info("Room id: " + reservation.getRoom().getId());
        }

        return "redirect:/hotelbooking/rooms";
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

    @GetMapping("/reservation")
    public String getRooms(Model model, HttpSession session){
        LOGGER.info("Navigating to reservation page");

        DateRangeDto dateRange = new DateRangeDto();
        model.addAttribute("dateRange", dateRange);
        model.addAttribute("roomType", session.getAttribute("roomType"));

        return "reservation";
    }
}
