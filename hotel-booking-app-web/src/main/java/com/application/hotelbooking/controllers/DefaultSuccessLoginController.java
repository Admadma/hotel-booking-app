package com.application.hotelbooking.controllers;

import com.application.hotelbooking.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "hotelbooking/default")
public class DefaultSuccessLoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSuccessLoginController.class);

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String defaultSuccessLogin(HttpServletRequest request){
        LOGGER.info("Redirecting user to the appropriate successful login page");

        if (userService.userHasRole(request.getUserPrincipal().getName(), "ADMIN")){
            return "redirect:/hotelbooking/admin";
        }

        return "redirect:/hotelbooking/home";
    }
}
