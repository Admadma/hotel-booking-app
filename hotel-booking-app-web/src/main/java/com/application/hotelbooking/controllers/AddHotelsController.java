package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.HotelCreationDTO;
import com.application.hotelbooking.exceptions.InvalidHotelException;
import com.application.hotelbooking.services.HotelService;
import com.application.hotelbooking.services.imagehandling.StorageException;
import com.application.hotelbooking.services.imagehandling.StorageService;
import com.application.hotelbooking.transformers.HotelViewTransformer;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(path = "hotelbooking/admin/add-hotels")
public class AddHotelsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddHotelsController.class);

    @Autowired
    private HotelService hotelService;

    @Autowired
    private HotelViewTransformer hotelViewTransformer;

    @Autowired
    private StorageService storageService;

    @PostMapping("/create-new-hotel")
    public String saveNewHotel(@Valid @ModelAttribute("hotelCreationDTO") HotelCreationDTO hotelCreationDTO, BindingResult result, Model model){
        if (result.hasErrors()){
            LOGGER.info("Error while validating");
            return "addhotels";
        }

        try {
            String imageName = storageService.store(hotelCreationDTO.getMultipartFile());
            hotelService.createHotel(hotelViewTransformer.transformToHotelCreationServiceDTO(hotelCreationDTO, imageName));
            model.addAttribute("successMessage", "Success");
        } catch (StorageException se){
            result.rejectValue("multipartFile", "admin.hotel.validation.image.save.error");
            return "addhotels";
        } catch (InvalidHotelException ihe){
            result.rejectValue("hotelName", "admin.hotel.validation.hotelname.taken");
            return "addhotels";
        } catch (Exception e){
            result.reject("admin.hotel.validation.global.error");
            LOGGER.info("Error while creating hotel");
            LOGGER.info(e.getMessage());
            return "addhotels";
        }

        return "addhotels";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException exceededException, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("fileUploadError", "Invalid file size");

        LOGGER.info("Handling MaxUploadSizeExceededException");
        return "redirect:/hotelbooking/admin/add-hotels";
    }

    @GetMapping("")
    public String addHotels(Model model, @ModelAttribute("fileUploadError") String fileUploadError){
        LOGGER.info("Navigating to addHotels page");

        if(!fileUploadError.isBlank()) {
            model.addAttribute("error", fileUploadError);
        }
        model.addAttribute("hotelCreationDTO", new HotelCreationDTO());
        return "addhotels";
    }
}
