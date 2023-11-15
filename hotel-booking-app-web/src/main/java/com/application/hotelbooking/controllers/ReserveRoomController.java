package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.ReservationView;
import com.application.hotelbooking.dto.ReservableRoomViewDTO;
import com.application.hotelbooking.exceptions.OutdatedReservationException;
import com.application.hotelbooking.services.ReservationService;
import com.application.hotelbooking.services.RoomService;
import com.application.hotelbooking.services.repositoryservices.RoomRepositoryService;
import com.application.hotelbooking.transformers.ReservationViewTransformer;
import com.application.hotelbooking.transformers.RoomSearchDTOTransformer;
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
    private RoomService roomService;

    @Autowired
    private RoomRepositoryService roomRepositoryService;

    @Autowired
    private RoomSearchDTOTransformer roomSearchDTOTransformer;

    @Autowired
    private ReservationViewTransformer reservationViewTransformer;

    @GetMapping("/reserve")
    public String reserve(@SessionAttribute("reservationPlan") ReservationView reservationView, HttpServletRequest request){
        try {
            reservationService.reserveRoom(reservationViewTransformer.transformToReservationModel(reservationView));
            LOGGER.info("Reserved room");
        } catch (OutdatedReservationException ore){
            LOGGER.info(ore.getMessage());
            return "redirect:/hotelbooking/home?reservationError";
        } catch (Exception e){
            LOGGER.info("Failed to reserve room");
            return "redirect:/hotelbooking/home?reservationError";
        } finally {
            request.getSession().removeAttribute("reservationPlan");
            request.getSession().removeAttribute("resultDTOS");
        }

        return "redirect:/hotelbooking/myreservations?reservationSuccess";
    }

    @GetMapping("/reserveroom")
    public String reserveRoom(@RequestParam("index") int index,
                              @SessionAttribute("resultDTOS") List<ReservableRoomViewDTO> reservableRoomViewDTOS,
                              HttpServletRequest request,
                              Authentication auth){
        LOGGER.info("Navigating to reserveroom page");

        request.getSession().setAttribute("reservationPlan", reservationViewTransformer.transformToReservationView(
                reservationService.prepareReservation(
                        roomSearchDTOTransformer.transformToRoomSearchResultDTO(
                                reservableRoomViewDTOS.get(index)),
                        auth.getName())));

        return "reserveroom";
    }
}
