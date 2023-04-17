package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.UserDto;
import com.application.hotelbooking.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "hotelbooking")
public class RegisterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register/add-new-user")
    public String addNewUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result){
        if (result.hasErrors()){
            LOGGER.info("Error while validating");
//            return "redirect:/hotelbooking/register?some_parameter_that_could_also_be_an_error_message";
            return "register";
        }
        userService.addNewUser(userDto.getUsername(), userDto.getPassword());
        LOGGER.info("Added user: " + userService.getUserByName(userDto.getUsername()));

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
