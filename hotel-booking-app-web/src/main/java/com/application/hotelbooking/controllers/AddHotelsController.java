package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.HotelView;
import com.application.hotelbooking.exceptions.InvalidHotelException;
import com.application.hotelbooking.services.HotelService;
import com.application.hotelbooking.transformers.HotelViewTransformer;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "hotelbooking/admin")
public class AddHotelsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddHotelsController.class);

    @Autowired
    private HotelService hotelService;

    @Autowired
    private HotelViewTransformer hotelViewTransformer;

    @PostMapping(value = "/create-new-hotel")
    public String saveNewHotel(@Valid @ModelAttribute("hotelView") HotelView hotelView, BindingResult result, Model model){
//        LOGGER.info("called create-new-hotel");
        if (result.hasErrors()){
            LOGGER.info("Error while validating");
//            result.addError(new ObjectError("globalError", "Failed to save hotel"));
            return "addhotels";
        }

        try {
            hotelService.createHotel(hotelViewTransformer.transformToRoomModel(hotelView));
            model.addAttribute("successMessage", "Success");
//            LOGGER.info("Created hotel " + hotelView.getId() + " " + hotelView.getHotelName() + " " + hotelView.getCity());
        } catch (InvalidHotelException ihe){
//            LOGGER.info("Failed to create hotel: " + ihe.getMessage());
//            result.addError(new ObjectError("globalError", "Failed to save hotel"));
            result.rejectValue("hotelName", "admin.hotel.validation.hotelname.taken");
            return "addhotels";
        } catch (Exception e){
            result.addError(new ObjectError("globalError", "Failed to save hotel"));
            return "addhotels";
        }

        return "addHotels";
    }

    @GetMapping("/addHotels")
    public String addRooms(Model model){
        LOGGER.info("Navigating to addRooms page");
        model.addAttribute("hotelView", new HotelView());
        return "addhotels";
    }
}
