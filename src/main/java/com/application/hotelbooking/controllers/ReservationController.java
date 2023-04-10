package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.services.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "hotelbooking")
public class ReservationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);


    @Autowired
    private ReservationService reservationService;

    @RequestMapping(value = "/book-room")
    public String bookRoom(@ModelAttribute("roomId") Long roomId){
        // TODO: later surround this with a try-catch. Exceptions can be for example: time period is invalid
        // TODO: user can edit the html code and change the room id. Code needs to check that there was no tampering with the parameters (e.g. clicking book this room but the id was changed to a different room)
        Reservation reservation = reservationService.createReservation(roomId);
//        Reservation reservation = familyRoomService.createReservation(roomId);

//        System.out.println("Successfully created reservation: " + reservation.getId() + " reserved room: " + reservation.getRoom().getId() + " user: " + reservation.getUser().getUsername());
        System.out.println("Successfully created reservation: " + reservation.getId() + " reserved room: "  + " user: " + reservation.getUser().getUsername());

        return "redirect:/hotelbooking/rooms";
    }


    @RequestMapping(value = "/select-room-type")
    public String getFamilyRoomData(@ModelAttribute("roomType") String roomType){

        System.out.println("There are "+ reservationService.countRoomsOfGivenType(roomType) + " rooms of type: " + roomType);

        Reservation reservation = reservationService.createReservationOnGivenRoomType(roomType);

        System.out.println("saved reservation");

        System.out.println("Successfully created reservation: " + reservation.getId() + " reserved room: " + reservation.getRoom().getId() + " user: " + reservation.getUser().getUsername());


        reservationService.getReservations().forEach(reservation1 -> System.out.println("Room id: " + reservation1.getRoom().getId()));
        System.out.println("Reservations for room of given id:");
        reservationService.getReservationsOfRoom(5l).forEach(reservation1 -> System.out.println("Room id: " + reservation1.getRoom().getId() + " start_date: " + reservation1.getStartDate()));


        return "redirect:/hotelbooking/rooms";
    }


}
