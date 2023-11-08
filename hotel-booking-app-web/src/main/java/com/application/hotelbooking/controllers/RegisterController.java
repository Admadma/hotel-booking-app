package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.UserFormDTO;
import com.application.hotelbooking.exceptions.UserAlreadyExistsException;
import com.application.hotelbooking.services.RoleService;
import com.application.hotelbooking.services.UserService;
import com.application.hotelbooking.transformers.RoleViewTransformer;
import com.application.hotelbooking.transformers.UserViewTransformer;
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

    @RequestMapping(value = "/register/add-new-user")
    public String addNewUser(@Valid @ModelAttribute("user") UserFormDTO userFormDTO, BindingResult result){
        if (result.hasErrors()){
            LOGGER.info("Error while validating");
            return "register";
        }

        try {
            userService.addNewUser(
                    userFormDTO.getUsername(),
                    userFormDTO.getPassword(),
                    roleService.getRoles(List.of("USER"))
            );
            LOGGER.info("back to controller");
            LOGGER.info("Added user: " + userViewTransformer.transformToUserView(userService.getUsersByName(userFormDTO.getUsername()).get(0)).getUsername());
        } catch (UserAlreadyExistsException uae) {
            result.rejectValue("username", null, "That name is already taken");
            LOGGER.info("That username is taken");
            return "register";
        } catch (Exception e){
            LOGGER.error("Failed to add user. Error message: " + e.getMessage());
            result.addError(new ObjectError("globalError", "Registration failed. Please use different credentials or try again later."));
            return "register";
        }

        return "redirect:/hotelbooking/home";
    }

    @GetMapping("/register")
    public String registration(Model model){
        LOGGER.info("Navigating to registration page");
        UserFormDTO user = new UserFormDTO();
        model.addAttribute("user", user);

        return "register";
    }
}
