package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.exceptions.InvalidUserException;
import com.application.hotelbooking.exceptions.NoRoomsAvailableException;
import com.application.hotelbooking.services.ReservationService;
import com.application.hotelbooking.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping(path = "hotelbooking")
public class ReservationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/select-room-type")
    public String reserveRoom(@ModelAttribute("roomType") String roomType, Authentication auth){
        // TODO: later surround this with a try-catch. Exceptions can be for example: time period is invalid
        // TODO: user can edit the html code and change the room id. Code needs to check that there was no tampering with the parameters (e.g. clicking book this room but the id was changed to a different room)
        try {
            Reservation reservation =reservationService.reserve(roomType, auth.getName(), LocalDate.now(), LocalDate.now().plusDays(7));
            LOGGER.info("Successfully created reservation: " + reservation.getId() + " reserved room: " + reservation.getRoom().getId() + " user: " + reservation.getUser().getUsername());
        } catch (InvalidUserException iue){
            // TODO: if this error occurs, maybe I should log out the user
            LOGGER.error(iue.getMessage());
        } catch (NoRoomsAvailableException nae) {
            LOGGER.error("Could not make a reservation because there are no free rooms of that type");
        }
        LOGGER.info("Id of rooms that have a reservation:");
        for (Reservation reservation: reservationService.getReservations()) {
            LOGGER.info("Room id: " + reservation.getRoom().getId());
        }

        return "redirect:/hotelbooking/rooms";
    }

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
}
