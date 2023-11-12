package com.application.hotelbooking.controllers;

import com.application.hotelbooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "hotelbooking")
public class ConfirmEmailController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/confirm-token")
    private String confirmToken(@RequestParam("confirmationToken") String confirmationToken){
        userService.confirmToken(confirmationToken);
        return "redirect:/hotelbooking/login";
    }
}
