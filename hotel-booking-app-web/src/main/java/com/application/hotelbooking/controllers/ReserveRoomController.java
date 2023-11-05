package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.RoomSearchResultViewDTO;
import com.application.hotelbooking.exceptions.InvalidTimePeriodException;
import com.application.hotelbooking.services.ReservationService;
import com.application.hotelbooking.services.RoomService;
import com.application.hotelbooking.services.repositoryservices.RoomRepositoryService;
import com.application.hotelbooking.transformers.RoomSearchDTOTransformer;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(path = "hotelbooking")
public class ReserveRoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReserveRoomController.class);

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepositoryService roomRepositoryService;

    @Autowired
    private RoomSearchDTOTransformer roomSearchDTOTransformer;

    @PostMapping("/reserve")
    public String reserve(@SessionAttribute("selectedRoom") RoomSearchResultViewDTO roomSearchResultViewDTO, Model model, Authentication auth){
        LOGGER.info("----------");

        LOGGER.info(String.valueOf(roomSearchResultViewDTO.getRoomNumber()));
        LOGGER.info(roomSearchResultViewDTO.getHotelName());
        LOGGER.info(String.valueOf(roomSearchResultViewDTO.getStartDate()));
        LOGGER.info("----------");

        LocalDate startDate = LocalDate.parse("2023-11-10");
        LOGGER.info(String.valueOf(startDate));
        LocalDate endDate = LocalDate.parse("2023-11-15");
        LOGGER.info(String.valueOf(endDate));
        int roomNumber = 323;
        LOGGER.info(String.valueOf(roomNumber));
        String hotelName = "asd Hotel";
        LOGGER.info(hotelName);


        try {
            reservationService.reserveRoom(
                    startDate,
                    endDate,
                    roomRepositoryService.findRoomByNumberAndHotelName(roomNumber, hotelName),
                    auth.getName());
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
    public String reserveRoom(@RequestParam("index") int index,
                              @SessionAttribute("resultDTOS") List<RoomSearchResultViewDTO> roomSearchResultViewDTOS,
                              Model model,
                              HttpServletRequest request){
        LOGGER.info("Navigating to reserveroom page");
        LOGGER.info(String.valueOf(index));
        LOGGER.info("---------------------");

        roomSearchDTOTransformer.transformToRoomSearchResultDTO(roomSearchResultViewDTOS.get(index));
//        request.getSession().setAttribute("selectedRoom", roomSearchDTOTransformer.transformToRoomSearchResultViewDTO(reservationService.prepareReservation(roomSearchDTOTransformer.transformToRoomSearchResultDTO(roomSearchResultViewDTOS.get(index)))));

        return "reserveroom";
    }
}
