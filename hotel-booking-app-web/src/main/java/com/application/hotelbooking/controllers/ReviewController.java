package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.ReservationStatus;
import com.application.hotelbooking.domain.ReservationView;
import com.application.hotelbooking.dto.ReviewDTO;
import com.application.hotelbooking.services.ReviewService;
import com.application.hotelbooking.services.repositoryservices.ReservationRepositoryService;
import com.application.hotelbooking.transformers.ReservationViewTransformer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping(path = "hotelbooking/review")
public class ReviewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReservationRepositoryService reservationRepositoryService;

    @Autowired
    private ReservationViewTransformer reservationViewTransformer;


    @PostMapping("/submit-review")
    public String submitReview(@Valid @ModelAttribute("reviewDTO") ReviewDTO reviewDTO, BindingResult result, HttpServletRequest request, Authentication auth){
        if (result.hasErrors()){
            LOGGER.info("Error while validating");
            return "reviewpage";
        }

        try {
            reviewService.createReview(
                    reviewDTO.getRating(),
                    reviewDTO.getComment(),
                    request.getSession().getAttribute("hotelName").toString(),
                    auth.getName());
            LOGGER.info("Created review.");
        } catch (Exception e){
            LOGGER.info("Error while makeing a review");
            return "reviewpage";
        }
        return "reviewpage";
    }

    @GetMapping("")
    public String reviews(@ModelAttribute("reservationUuid") UUID uuid, Model model, HttpServletRequest request){
        LOGGER.info("Navigating to review page");

        ReservationView reservationView = reservationViewTransformer.transformToReservationView(reservationRepositoryService.getReservationByUuid(uuid).get());

        if (ReservationStatus.COMPLETED.equals(reservationView.getReservationStatus())){
            request.getSession().setAttribute("hotelName", reservationView.getRoom().getHotel().getHotelName());
            request.getSession().setAttribute("hotelImageName", reservationView.getRoom().getHotel().getImageName());
            model.addAttribute("reviewDTO", new ReviewDTO());
            return "reviewpage";
        } else {
            return "redirect:/hotelbooking/my-reservations";
        }
    }
}
