package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.RoomSearchFormDTO;
import com.application.hotelbooking.services.NewReservationService;
import com.application.hotelbooking.services.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReserveRoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReserveRoomController.class);

    @Autowired
    private NewReservationService reservationService;

//    @PostMapping("/reserve")
//    public String reserve(){
//
//        reservationService.
//    }

    @GetMapping("/reserveroom")
    public String reserveRoom(@RequestParam("roomNumber") String roomNumber,
                              @RequestParam("hotelName") String hotelName,
                              @RequestParam("arrivalDate") String startDate,
                              @RequestParam("departureDate") String endDate,
                              Model model){
        LOGGER.info("Navigating to reserveroom page");

        model.addAttribute("hotelname", roomNumber);
        model.addAttribute("hotelName", hotelName);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        //My entity will require: roomNumber, hotelName, startDate, endDate

//        model.addAttribute("roomSearchFormDTO", new RoomSearchFormDTO());

        return "reserveroom";
    }
}
