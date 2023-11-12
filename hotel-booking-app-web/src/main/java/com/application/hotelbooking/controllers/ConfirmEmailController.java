package com.application.hotelbooking.controllers;

import com.application.hotelbooking.exceptions.EmailAlreadyConfirmedException;
import com.application.hotelbooking.exceptions.ExpiredTokenException;
import com.application.hotelbooking.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "hotelbooking")
public class ConfirmEmailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmEmailController.class);

    @Autowired
    private UserService userService;

    @GetMapping(value = "/confirm-token")
    private String confirmToken(@RequestParam("confirmationToken") String confirmationToken){
        try{
            userService.confirmToken(confirmationToken);
        } catch (EmailAlreadyConfirmedException eac){
            LOGGER.info("Email already confirmed.");
            return "redirect:/hotelbooking/confirmemail?emailAlreadyConfirmed";
        } catch (ExpiredTokenException ete){
            LOGGER.info("Token already expired");
            return "redirect:/hotelbooking/confirmemail?tokenAlreadyExpired";
        }
        return "redirect:/hotelbooking/login";
    }

    @PostMapping(value = "/send-new-token")
    private String sendNewToken(@SessionAttribute("email") String email){
        LOGGER.info("send-new-token");
        LOGGER.info(email);
        return "confirmemail";
    }

    @GetMapping("/confirmemail")
    private String confirmEmail(Model model, HttpServletRequest request){
        LOGGER.info("Nvaigating to confirmemail page");
        model.addAttribute("email", request.getSession().getAttribute("email"));

        return "confirmemail";
    }
}
