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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/reserveroom")
    public String reserveRoom(@RequestParam("hotelName") String hotelName,
                              @RequestParam("number") int roomNumber,
                              @SessionAttribute("hotelsRoomsResultDTOs") List<HotelWithReservableRoomsDTO> hotelsWithReservableRoomsDTOS,
                              HttpServletRequest request,
                              Authentication auth){
        LOGGER.info("Navigating to reserveroom page");
        System.out.println("Clicked on roomnumber");
        System.out.println(roomNumber);

//        hotelsWithReservableRoomsDTOS.stream().flatMap(hotelWithReservableRoomsDTO -> hotelWithReservableRoomsDTO.getUniqueReservableRoomOfHotelDTOList()). .filter(hotelWithReservableRoomsDTO -> hotelWithReservableRoomsDTO.getHotelName().equals(hotelName))
        System.out.println(hotelsWithReservableRoomsDTOS.stream().flatMap(hotelWithReservableRoomsDTO -> hotelWithReservableRoomsDTO.getUniqueReservableRoomOfHotelDTOList().stream()).collect(Collectors.toList()));;

//        hotelsWithReservableRoomsDTOS.stream().flatMap(hotelWithReservableRoomsDTO -> hotelWithReservableRoomsDTO.getUniqueReservableRoomOfHotelDTOList().stream().map(uniqueReservableRoomOfHotelDTOs -> new HotelWithReservableRoomsDTO(hotelWithReservableRoomsDTO.getHotelName(),hotelWithReservableRoomsDTO.getCity(), hotelWithReservableRoomsDTO.getImageName(), hotelWithReservableRoomsDTO.getAverageRating(), uniqueReservableRoomOfHotelDTO))). .filter(hotelWithReservableRoomsDTO -> hotelWithReservableRoomsDTO.getHotelName().equals(hotelName))
//
//        hotelsWithReservableRoomsDTOS.stream().filter(hotelWithReservableRoomsDTO -> hotelWithReservableRoomsDTO.getHotelName().equals(hotelName)).findFirst().get().getUniqueReservableRoomOfHotelDTOList().stream().filter(uniqueReservableRoomOfHotelDTO -> uniqueReservableRoomOfHotelDTO.getNumber() == roomNumber).
        // take the hotel name and room number -> search for this exact room -> return its details to the user on confirmReservation page -> if it was taken, then return another similar one

        //TODO: transform to view
        try {
            request.getSession().setAttribute("reservationPlan",
                    reservationService.fixedPrepareReservationNew(roomNumber, hotelName, hotelsWithReservableRoomsDTOTransformer.transformToHotelWithReservableRoomsServiceDTOs(hotelsWithReservableRoomsDTOS)));
        } catch (OutdatedReservationException ore) {
            LOGGER.info(ore.getMessage());
            return "redirect:/hotelbooking/home?reservationError";
        }

//        System.out.println("reservation plan:");
//        System.out.println(reservationService.fixedPrepareReservationNew(roomNumber, hotelName, hotelsWithReservableRoomsDTOTransformer.transformToHotelWithReservableRoomsServiceDTOs(hotelsWithReservableRoomsDTOS)));
//
//        try {
//            request.getSession().setAttribute("reservationPlan", reservationViewTransformer.transformToReservationView(
//                    reservationService.prepareReservationNew(hotelName, hotelsWithReservableRoomsDTOTransformer.transformToHotelWithReservableRoomsServiceDTOs(hotelsWithReservableRoomsDTOS), auth.getName())));
//        } catch (OutdatedReservationException ore) {
//            LOGGER.info(ore.getMessage());
//            return "redirect:/hotelbooking/home?reservationError";
//        }

        return "reserveroom";
    }
}
