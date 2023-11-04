package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.RoomSearchFormDTO;
import com.application.hotelbooking.dto.RoomSearchResultViewDTO;
import com.application.hotelbooking.exceptions.InvalidTimePeriodException;
import com.application.hotelbooking.services.NewReservationService;
import com.application.hotelbooking.services.RoomService;
import com.application.hotelbooking.transformers.RoomSearchDTOTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping(path = "hotelbooking")
public class ReserveRoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReserveRoomController.class);

    @Autowired
    private NewReservationService reservationService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomSearchDTOTransformer roomSearchDTOTransformer;

    @GetMapping("/reserve")
    public String reserve(@ModelAttribute("testAttribute") String testAttribute, Model model){
//        @ModelAttribute("hotelName") String hotelName, @ModelAttribute("roomNumber") Integer roomNumber,
//        LOGGER.info(hotelName);

        LOGGER.info("value:|" + testAttribute + "|");

        LocalDate startDate = LocalDate.parse("2023-11-06");
        LOGGER.info(String.valueOf(startDate));
        LocalDate endDate = LocalDate.parse("2023-11-10");
        LOGGER.info(String.valueOf(endDate));
        int roomNumber = 323;
        LOGGER.info(String.valueOf(roomNumber));
        String hotelName = "asd Hotel";
        LOGGER.info(hotelName);


        try {
            reservationService.reserveRoom(
                    startDate,
                    endDate,
                    roomService.findRoomByNumberAndHotelName(roomNumber, hotelName), "test_user");
            LOGGER.info("Reserved room");
        } catch (InvalidTimePeriodException itpe){
            LOGGER.info("Time period taken or invalid");
        }catch (Exception e){
            LOGGER.info("Failed to reserve room");
            LOGGER.info(String.valueOf(e.getClass()));
        }
        return "home";
    }

    @GetMapping("/reserveroom")
    public String reserveRoom(@RequestParam("roomNumber") String roomNumber,
                              @RequestParam("hotelName") String hotelName,
                              @RequestParam("arrivalDate") String startDate,
                              @RequestParam("departureDate") String endDate,
                              Model model){
        LOGGER.info("Navigating to reserveroom page");

        LOGGER.info("roomNumber: " + roomNumber);
        LOGGER.info("hotelName: " + hotelName);
        LOGGER.info("startDate: " + startDate);
        LOGGER.info("endDate: " + endDate);
        LOGGER.info("---------------------");

        model.addAttribute("roomNumber", roomNumber);
        model.addAttribute("hotelName", hotelName);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        model.addAttribute("testAttribute", "test");


        return "reserveroom";
    }
}
