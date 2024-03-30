package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.RoomType;
import com.application.hotelbooking.domain.RoomView;
import com.application.hotelbooking.dto.ReservableHotelDTO;
import com.application.hotelbooking.dto.RoomSearchFormDTO;
import com.application.hotelbooking.dto.ReservableRoomViewDTO;
import com.application.hotelbooking.services.RoomService;
import com.application.hotelbooking.services.repositoryservices.HotelRepositoryService;
import com.application.hotelbooking.transformers.HotelViewTransformer;
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

import java.util.Arrays;
import java.util.List;
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

    private void transformFieldsToNulls(RoomSearchFormDTO roomSearchFormDTO){
        if ("".equals(roomSearchFormDTO.getCity())){
            roomSearchFormDTO.setCity(null);
        }
        if ("".equals(roomSearchFormDTO.getHotelName())){
            roomSearchFormDTO.setHotelName(null);
        }
    }

//    @PostMapping(value = "/search-rooms")
//    public String searchRooms(@Valid @ModelAttribute("roomSearchFormDTO") RoomSearchFormDTO roomSearchFormDTO, BindingResult result, HttpServletRequest request, RedirectAttributes redirectAttributes, Model model){
//        if (result.hasErrors()){
//            LOGGER.info("Error while validating");
//            return "homepage";
//        }
//
//        if (!roomService.isEndDateAfterStartDate(roomSearchFormDTO.getStartDate(), roomSearchFormDTO.getEndDate())){
//            result.rejectValue("startDate", "home.room.form.validation.startdate.must.before");
//            result.rejectValue("endDate", "home.room.form.validation.enddate.must.after");
//            return "homepage";
//        }
//
//        transformFieldsToNulls(roomSearchFormDTO);
//
//        List<ReservableRoomViewDTO> resultDTOS = roomSearchDTOTransformer.transformToRoomSearchResultViewDTOs(roomService.searchRooms(roomSearchDTOTransformer.transformToRoomSearchFormServiceDTO(roomSearchFormDTO)));
//        request.getSession().setAttribute("resultDTOS", resultDTOS);
//        LOGGER.info(resultDTOS.toString());
//
//        LOGGER.info("No Error");
//        redirectAttributes.addFlashAttribute("successRoomSearchFormDTO", roomSearchFormDTO);
//        return "redirect:/hotelbooking/home";
//    }


    @GetMapping(value = "/newHome")
    public String homeWithParams(Model model, @ModelAttribute("successRoomSearchFormDTO") RoomSearchFormDTO roomSearchFormDTO, HttpServletRequest request){
        LOGGER.info("Navigating to home page");

        String hotelName = "Hotel_1";
        String city = "City_1";
        RoomView roomView = RoomView.builder().singleBeds(1).build();
        RoomView roomView2 = RoomView.builder().singleBeds(3).build();


        List<ReservableHotelDTO> newResultDTOS = List.of(new ReservableHotelDTO(hotelName, city, List.of(roomView)), new ReservableHotelDTO(hotelName, city, List.of(roomView, roomView2)));
        request.getSession().setAttribute("newResultDTOS", newResultDTOS);

        model.addAttribute("roomSearchFormDTO", roomSearchFormDTO);

        return "newhomepage";
    }
}
