package com.application.hotelbooking.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "hotelbooking/admin")
public class AdminPageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminPageController.class);

    @GetMapping("")
    public String admin(){
        LOGGER.info("Navigating to admin page");

        return "adminpage";
    }
}
