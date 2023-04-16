package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.UserDto;
import com.application.hotelbooking.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "hotelbooking")
public class RegisterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register/add-new-user")
    public String addNewUser(@ModelAttribute("user") UserDto userDto){
        LOGGER.info("Adding new user");
        LOGGER.info("Username: " + userDto.getUsername());
//        userService.addUser(null);

        return "redirect:/hotelbooking/home";
    }

    @GetMapping("/register")
    public String registration(Model model){
        LOGGER.info("Navigating to registration page");
        UserDto user = new UserDto();
        model.addAttribute("user", user);

        return "register";
    }
}
