package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.UserView;
import com.application.hotelbooking.dto.ChangeCredentialsDto;
import com.application.hotelbooking.services.UserService;
import com.application.hotelbooking.transformers.UserViewTransformer;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "hotelbooking")
public class ChangeCredentialsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserViewTransformer userViewTransformer;

    @RequestMapping(value = "/change-password")
    public String changePassword(@ModelAttribute("credentials") ChangeCredentialsDto changeCredentialsDto, BindingResult result, Authentication auth, HttpSession session, Model model){

        Long version = Long.valueOf(session.getAttribute("version").toString());
        String oldPW = userService.getUserByName(auth.getName()).get(0).getPassword();

        userService.changePassword(auth.getName(),changeCredentialsDto.getNewPassword(), version);
        LOGGER.info("old password: " + oldPW);
        LOGGER.info("new password: " + userService.getUserByName(auth.getName()).get(0).getPassword());

        return "account";
    }

    @GetMapping("/account")
    public String changeCredentials(HttpSession session, Authentication auth, Model model){
        LOGGER.info("Navigating to account page");
        ChangeCredentialsDto changeCredentialsDto = new ChangeCredentialsDto();
        model.addAttribute("credentials", changeCredentialsDto);
        session.setAttribute("version", userViewTransformer.transformToUserView(userService.getUserByName(auth.getName()).get(0)).getVersion());
        return "account";
    }
}
