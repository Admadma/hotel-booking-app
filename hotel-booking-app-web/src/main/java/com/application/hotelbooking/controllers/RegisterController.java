package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.NewUserFormDTO;
import com.application.hotelbooking.dto.RoomCreationDTO;
import com.application.hotelbooking.dto.UserFormDTO;
import com.application.hotelbooking.exceptions.EmailAlreadyExistsException;
import com.application.hotelbooking.exceptions.UserAlreadyExistsException;
import com.application.hotelbooking.services.RoleService;
import com.application.hotelbooking.services.UserService;
import com.application.hotelbooking.transformers.RoleViewTransformer;
import com.application.hotelbooking.transformers.UserViewTransformer;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "hotelbooking")
public class RegisterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserViewTransformer userViewTransformer;
    @Autowired
    private RoleViewTransformer roleViewTransformer;

    @RequestMapping(value = "register/create-new-user")
    public String createUser(@Valid @ModelAttribute("newUserFormDTO") NewUserFormDTO newUserFormDTO, BindingResult result){
        if (result.hasErrors()){
            LOGGER.info("Error while validating newUserFormDTO");
            return "register";
        }

        try {
            LOGGER.info("Validating email");
            InternetAddress internetAddress = new InternetAddress(newUserFormDTO.getEmail());
            internetAddress.validate();
            LOGGER.info("creating...");
            userService.createUser(newUserFormDTO.getUsername(), newUserFormDTO.getPassword(), newUserFormDTO.getEmail(), List.of("USER"));
            LOGGER.info("...created");
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
            result.addError(new ObjectError("globalError", "Registration failed. Please use different credentials or try again later."));
            return "register";
        }
        //TODO: home page will be accessible for unauthenticated user as well, so it's okay to redirect there. But login will be needed once a room is selected
        //TODO: introduce a simple page that informs the user of the sent verification email
        return "redirect:/hotelbooking/home";
    }

    @GetMapping("/register")
    public String registration(Model model){
        LOGGER.info("Navigating to registration page");
        UserFormDTO user = new UserFormDTO();
        model.addAttribute("user", user);
        model.addAttribute("newUserFormDTO", new NewUserFormDTO());

        return "register";
    }
}
