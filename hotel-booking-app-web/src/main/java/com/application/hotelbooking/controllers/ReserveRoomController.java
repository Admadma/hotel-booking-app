package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.*;
import com.application.hotelbooking.exceptions.OutdatedReservationException;
import com.application.hotelbooking.services.ReservationService;
import com.application.hotelbooking.transformers.HotelsWithReservableRoomsDTOTransformer;
import com.application.hotelbooking.transformers.ReservationPlanTransformer;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "hotelbooking")
public class ReserveRoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReserveRoomController.class);

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationPlanTransformer reservationPlanTransformer;
    @Autowired
    private HotelsWithReservableRoomsDTOTransformer hotelsWithReservableRoomsDTOTransformer;

    @PostMapping("/reserve")
    public String reserve(@SessionAttribute("reservationPlan") ReservationPlanDTO reservationPlanDTO, HttpServletRequest request, Authentication auth){
        try {
            reservationService.reserveRoom(reservationPlanTransformer.transformToReservationPlanServiceDTO(reservationPlanDTO), auth.getName());
            LOGGER.info("Reserved room");
        } catch (OutdatedReservationException ore){
            LOGGER.info(ore.getMessage());
            return "redirect:/hotelbooking/home?reservationError";
        } catch (Exception e){
            LOGGER.info("Failed to reserve room");
            LOGGER.info(e.getMessage());
            return "redirect:/hotelbooking/home?reservationError";
        } finally {
            request.getSession().removeAttribute("reservationPlan");
            request.getSession().removeAttribute("hotelsRoomsResultDTOs");
        }

        return "redirect:/hotelbooking/myreservations?reservationSuccess";
    }

    @GetMapping("/reserveroom")
    public String reserveRoom(@RequestParam("hotelName") String hotelName,
                              @RequestParam("number") int roomNumber,
                              @SessionAttribute("hotelsRoomsResultDTOs") List<HotelWithReservableRoomsDTO> hotelsWithReservableRoomsDTOS,
                              HttpServletRequest request){
        LOGGER.info("Navigating to reserveroom page");

        request.getSession().setAttribute("reservationPlan",
                reservationPlanTransformer.transformToReservationPlanDTO(
                        reservationService.createReservationPlan(
                                roomNumber,
                                hotelName,
                                hotelsWithReservableRoomsDTOTransformer.transformToHotelWithReservableRoomsServiceDTOs(hotelsWithReservableRoomsDTOS)
                        )
                )
        );

        return "reserveroom";
    }
}
