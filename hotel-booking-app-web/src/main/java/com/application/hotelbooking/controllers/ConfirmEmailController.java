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
@RequestMapping(path = "hotelbooking/register/confirm-email")
public class ConfirmEmailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmEmailController.class);

    @Autowired
    private UserEmailTokenConfirmationService userEmailTokenConfirmationService;
    @Autowired
    private ResendConfirmationTokenService resendConfirmationTokenService;

    @GetMapping("/confirm-token")
    private String confirmToken(@RequestParam("confirmationToken") String confirmationToken){
        try{
            userEmailTokenConfirmationService.confirmToken(confirmationToken);
        } catch (InvalidTokenException itc){
            LOGGER.info("Invalid token.");
            return "redirect:/hotelbooking/register/confirm-email?invalidLink";
        } catch (EmailAlreadyConfirmedException eac){
            LOGGER.info("Email already confirmed.");
            return "redirect:/hotelbooking/register/confirm-email?emailAlreadyConfirmed";
        } catch (ExpiredTokenException ete){
            LOGGER.info("Token already expired");
            return "redirect:/hotelbooking/register/confirm-email?tokenAlreadyExpired";
        } catch (Exception e){
            LOGGER.info("Error while validating token");
            LOGGER.info(e.getMessage());
            return "redirect:/hotelbooking/register/confirm-email?error";
        }
        return "redirect:/hotelbooking/login";
    }

    @PostMapping("/send-new-token")
    private String sendNewToken(@SessionAttribute("email") String email){
        LOGGER.info("send-new-token");
        try {
            resendConfirmationTokenService.resendConfirmationToken(email);
        } catch (InvalidUserException iue) {
            LOGGER.info("There is no user with that email");
            return "redirect:/hotelbooking/confirm-email?invalidUser";
        } catch (EmailAlreadyConfirmedException eac){
            LOGGER.info("Email already confirmed.");
            return "redirect:/hotelbooking/confirm-email?emailAlreadyConfirmed";
        } catch (Exception e){
            LOGGER.info("Error while resending confirmation token");
            LOGGER.info(e.getMessage());
            return "redirect:/hotelbooking/confirm-email?resendError";
        }
        return "confirmemail";
    }

    @GetMapping("")
    private String confirmEmail(Model model, HttpServletRequest request){
        LOGGER.info("Nvaigating to confirmemail page");
        model.addAttribute("email", request.getSession().getAttribute("email"));

        return "confirmemail";
    }
}
