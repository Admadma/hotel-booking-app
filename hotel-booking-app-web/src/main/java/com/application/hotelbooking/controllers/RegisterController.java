package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.RoleModel;
import com.application.hotelbooking.dto.UserDto;
import com.application.hotelbooking.exceptions.UserAlreadyExistsException;
import com.application.hotelbooking.repositories.RoleRepository;
import com.application.hotelbooking.services.UserService;
import com.application.hotelbooking.transformers.RoleTransformer;
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

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "hotelbooking")
public class RegisterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleTransformer roleTransformer;

    @RequestMapping(value = "/register/add-new-user")
    public String addNewUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result){
        if (result.hasErrors()){
            LOGGER.info("Error while validating");
            return "register";
        }

        Collection<RoleModel> roles= List.of(roleRepository.findRoleByName("USER")).stream().map(role -> roleTransformer.transformToModel(role)).collect(Collectors.toList());
        roles.forEach(roleModel -> System.out.println(roleModel.getName()));

        try {
            userService.addNewUser(
                    userDto.getUsername(),
                    userDto.getPassword(),
                    List.of(roleRepository.findRoleByName("USER")).stream().map(role -> roleTransformer.transformToModel(role)).collect(Collectors.toList()) //TODO: use something other than repository
            );
//            System.out.println("added user");
//            System.out.println(userDto.getUsername());
//            System.out.println(userService.getUserByName(userDto.getUsername()));
            LOGGER.info("Added user: " + userService.getUserByName(userDto.getUsername()).get(0).getUsername());
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
        UserDto user = new UserDto();
        model.addAttribute("user", user);

        return "register";
    }
}
