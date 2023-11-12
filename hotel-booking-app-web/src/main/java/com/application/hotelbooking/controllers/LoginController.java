package com.application.hotelbooking.controllers;

import com.application.hotelbooking.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "hotelbooking")
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @GetMapping(value = "/confirm-token")
    private String confirmToken(@RequestParam("confirmationToken") String confirmationToken){
        LOGGER.info("confirming");
        userService.confirmToken(confirmationToken);
        LOGGER.info("token confirmed");
        return "redirect:/hotelbooking/login";
    }

    @GetMapping("/login")
    public String login(){
        if (isAuthenticated()){
            return "redirect:/hotelbooking/home";
        }
        return "login";
    }
}
