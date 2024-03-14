package com.application.hotelbooking.controllers;

import com.application.hotelbooking.exceptions.EmailAlreadyConfirmedException;
import com.application.hotelbooking.exceptions.ExpiredTokenException;
import com.application.hotelbooking.exceptions.InvalidTokenException;
import com.application.hotelbooking.exceptions.InvalidUserException;
import com.application.hotelbooking.services.ResendConfirmationTokenService;
import com.application.hotelbooking.services.UserEmailTokenConfirmationService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "hotelbooking/register")
public class ConfirmEmailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmEmailController.class);

    @Autowired
    private UserEmailTokenConfirmationService userEmailConfirmationService;
    @Autowired
    private ResendConfirmationTokenService resendConfirmationTokenService;

    @GetMapping(value = "/confirmemail/confirm-token")
    private String confirmToken(@RequestParam("confirmationToken") String confirmationToken){
        try{
            userEmailConfirmationService.confirmToken(confirmationToken);
        } catch (InvalidTokenException itc){
            LOGGER.info("Invalid token.");
            return "redirect:/hotelbooking/register/confirmemail?invalidLink";
        } catch (EmailAlreadyConfirmedException eac){
            LOGGER.info("Email already confirmed.");
            return "redirect:/hotelbooking/register/confirmemail?emailAlreadyConfirmed";
        } catch (ExpiredTokenException ete){
            LOGGER.info("Token already expired");
            return "redirect:/hotelbooking/register/confirmemail?tokenAlreadyExpired";
        }
        return "redirect:/hotelbooking/login";
    }

    @PostMapping(value = "/confirmemail/send-new-token")
    private String sendNewToken(@SessionAttribute("email") String email){
        LOGGER.info("send-new-token");
        try {
            resendConfirmationTokenService.resendConfirmationToken(email);
        } catch (InvalidUserException iue){
            LOGGER.info("There is no user with that email");
            return "redirect:/hotelbooking/confirmemail?invalidUser";
        } catch (EmailAlreadyConfirmedException eac){
            LOGGER.info("Email already confirmed.");
            return "redirect:/hotelbooking/confirmemail?emailAlreadyConfirmed";
        }
        return "confirmemail";
    }

    @GetMapping("/confirmemail")
    private String confirmEmail(Model model, HttpServletRequest request){
        LOGGER.info("Nvaigating to confirmemail page");
        model.addAttribute("email", request.getSession().getAttribute("email"));

        return "confirmemail";
    }
}
