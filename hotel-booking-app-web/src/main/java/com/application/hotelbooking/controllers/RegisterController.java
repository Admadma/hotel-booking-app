package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.NewUserFormDTO;
import com.application.hotelbooking.exceptions.EmailAlreadyExistsException;
import com.application.hotelbooking.exceptions.UserAlreadyExistsException;
import com.application.hotelbooking.services.UserService;
import com.application.hotelbooking.validators.InternetAddressValidator;
import jakarta.mail.internet.AddressException;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.List;

@Controller
@RequestMapping(path = "hotelbooking/register")
public class RegisterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private InternetAddressValidator internetAddressValidator;

    @RequestMapping("/create-new-user")
    public String createUser(@Valid @ModelAttribute("newUserFormDTO") NewUserFormDTO newUserFormDTO, BindingResult result, HttpServletRequest request){
        if (result.hasErrors()){
            LOGGER.info("Error while validating newUserFormDTO");
            return "register";
        }

        try {
            internetAddressValidator.validate(newUserFormDTO.getEmail());
            userService.createUser(newUserFormDTO.getUsername(), newUserFormDTO.getPassword(), newUserFormDTO.getEmail(), List.of("USER"));
        } catch (AddressException ae) {
            result.rejectValue("email", "registration.error.email.invalid", "That email is invalid");
            LOGGER.info("That email is invalid");
            return "register";
        } catch (UserAlreadyExistsException uae) {
            result.rejectValue("username", "registration.error.username.taken", "That name is already taken");
            LOGGER.info("That username is taken");
            return "register";
        } catch (EmailAlreadyExistsException eae){
            result.rejectValue("email", "registration.error.email.taken", "That email is already taken");
            LOGGER.info("That email is already taken");
            return "register";
        } catch (Exception e){
            LOGGER.error("Failed to add user. Error message: " + e.getMessage());
            result.reject("registration.generic.global.error");
            return "register";
        }

        request.getSession().setAttribute("email", newUserFormDTO.getEmail());
        return "redirect:/hotelbooking/register/confirmemail";
    }

    @GetMapping("")
    public String registration(Model model){
        LOGGER.info("Navigating to registration page");
        model.addAttribute("newUserFormDTO", new NewUserFormDTO());

        return "register";
    }
}
