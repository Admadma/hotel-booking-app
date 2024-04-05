package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.ReservationView;
import com.application.hotelbooking.domain.RoomType;
import com.application.hotelbooking.dto.HotelWithReservableRoomsDTO;
import com.application.hotelbooking.dto.ReservableRoomViewDTO;
import com.application.hotelbooking.dto.UniqueReservableRoomOfHotelDTO;
import com.application.hotelbooking.exceptions.OutdatedReservationException;
import com.application.hotelbooking.services.ReservationService;
import com.application.hotelbooking.transformers.HotelsWithReservableRoomsDTOTransformer;
import com.application.hotelbooking.transformers.ReservationViewTransformer;
import com.application.hotelbooking.transformers.RoomSearchDTOTransformer;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "hotelbooking")
public class ReserveRoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReserveRoomController.class);

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RoomSearchDTOTransformer roomSearchDTOTransformer;

    @Autowired
    private ReservationViewTransformer reservationViewTransformer;
    @Autowired
    private HotelsWithReservableRoomsDTOTransformer hotelsWithReservableRoomsDTOTransformer;

    @PostMapping("/reserve")
    public String reserve(@SessionAttribute("reservationPlan") ReservationView reservationView, HttpServletRequest request){
        try {
            reservationService.reserveRoom(reservationViewTransformer.transformToReservationModel(reservationView));
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

    @GetMapping("/reserveroomNew")
    public String reserveRoomNew(@RequestParam("hotelName") String hotelName,
                                 @SessionAttribute("hotelsRoomsResultDTOs") List<HotelWithReservableRoomsDTO> hotelsWithReservableRoomsDTOS,
                              HttpServletRequest request,
                              Authentication auth){
        LOGGER.info("Navigating to reserveroom page");

        try {
            request.getSession().setAttribute("reservationPlan", reservationViewTransformer.transformToReservationView(
                    reservationService.prepareReservationNew(hotelName, hotelsWithReservableRoomsDTOTransformer.transformToHotelWithReservableRoomsServiceDTOs(hotelsWithReservableRoomsDTOS), auth.getName())));
        } catch (OutdatedReservationException ore) {
            LOGGER.info(ore.getMessage());
            return "redirect:/hotelbooking/home?reservationError";
        }

        return "reserveroom";
    }
}
