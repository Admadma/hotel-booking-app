package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.RoomType;
import com.application.hotelbooking.dto.HotelWithReservableRoomsDTO;
import com.application.hotelbooking.dto.RoomSearchFormDTO;
import com.application.hotelbooking.services.RoomService;
import com.application.hotelbooking.services.repositoryservices.HotelRepositoryService;
import com.application.hotelbooking.transformers.HotelViewTransformer;
import com.application.hotelbooking.transformers.HotelsWithReservableRoomsDTOTransformer;
import com.application.hotelbooking.transformers.RoomSearchDTOTransformer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "hotelbooking")
public class NewHomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewHomeController.class);

    @Autowired
    private RoomService roomService;

    @Autowired
    private HotelRepositoryService hotelRepositoryService;

    @Autowired
    private RoomSearchDTOTransformer roomSearchDTOTransformer;

    @Autowired
    private HotelViewTransformer hotelViewTransformer;
    @Autowired
    private HotelsWithReservableRoomsDTOTransformer hotelsWithReservableRoomsDTOTransformer;

    private void transformFieldsToNulls(RoomSearchFormDTO roomSearchFormDTO){
        if ("".equals(roomSearchFormDTO.getCity())){
            roomSearchFormDTO.setCity(null);
        }
        if ("".equals(roomSearchFormDTO.getHotelName())){
            roomSearchFormDTO.setHotelName(null);
        }
    }

    @PostMapping(value = "/search-rooms-new")
//    public String searchRooms(@Valid @ModelAttribute("roomSearchFormDTO") RoomSearchFormDTO roomSearchFormDTO, BindingResult result, HttpServletRequest request, RedirectAttributes redirectAttributes, Model model){
    public String searchRooms(@ModelAttribute("roomSearchFormDTO") RoomSearchFormDTO roomSearchFormDTO, BindingResult result, HttpServletRequest request, RedirectAttributes redirectAttributes, Model model){
        // Temporarily skipping date selection to make debugging faster
        roomSearchFormDTO.setStartDate(LocalDate.now());
        roomSearchFormDTO.setEndDate(LocalDate.now().plusDays(5));
        if (result.hasErrors()){
            LOGGER.info("Error while validating");
            return "newhomepage";
        }

        if (!roomService.isEndDateAfterStartDate(roomSearchFormDTO.getStartDate(), roomSearchFormDTO.getEndDate())){
            result.rejectValue("startDate", "home.room.form.validation.startdate.must.before");
            result.rejectValue("endDate", "home.room.form.validation.enddate.must.after");
            return "newhomepage";
        }

        transformFieldsToNulls(roomSearchFormDTO);
        List<HotelWithReservableRoomsDTO> hotelsWithReservableRoomsDTOS = hotelsWithReservableRoomsDTOTransformer.transformToHotelsWithReservableRoomsDTOs(roomService.searchHotelsWithReservableRooms(roomSearchDTOTransformer.transformToRoomSearchFormServiceDTO(roomSearchFormDTO)));

        if (hotelsWithReservableRoomsDTOS.size() > 0) {
            request.getSession().setAttribute("resultDTOS", hotelsWithReservableRoomsDTOS);
            request.getSession().removeAttribute("noMatchingResults");
            return "redirect:/hotelbooking/newHome";
        }

        request.getSession().removeAttribute("resultDTOS");
        request.getSession().setAttribute("noMatchingResults", true);
        return "redirect:/hotelbooking/newHome";
    }


    @GetMapping(value = "/newHome")
    public String homeWithParams(Model model, @ModelAttribute("successRoomSearchFormDTO") RoomSearchFormDTO roomSearchFormDTO, HttpServletRequest request){
        LOGGER.info("Navigating to home page");

        model.addAttribute("roomSearchFormDTO", roomSearchFormDTO);

        request.getSession().setAttribute("roomTypes", Arrays.stream(RoomType.values()).map(roomType -> roomType.name()).collect(Collectors.toList()));
        request.getSession().setAttribute("hotels", hotelViewTransformer.transformToHotelViews(hotelRepositoryService.getAllHotels()));
        request.getSession().setAttribute("cities", hotelRepositoryService.getAllHotels().stream().map(hotelModel -> hotelModel.getCity()).collect(Collectors.toList()));

        // List all the different rooms to the user within a set date interval
        if (Objects.isNull(request.getSession().getAttribute("resultDTOS"))) {
            roomSearchFormDTO.setStartDate(LocalDate.now().plusDays(7));
            roomSearchFormDTO.setEndDate(LocalDate.now().plusDays(14));
            List<HotelWithReservableRoomsDTO> hotelsWithReservableRoomsDTOS = hotelsWithReservableRoomsDTOTransformer.transformToHotelsWithReservableRoomsDTOs(roomService.searchHotelsWithReservableRooms(roomSearchDTOTransformer.transformToRoomSearchFormServiceDTO(roomSearchFormDTO)));
            request.getSession().setAttribute("resultDTOS", hotelsWithReservableRoomsDTOS);
        }

        return "newhomepage";
    }
}
