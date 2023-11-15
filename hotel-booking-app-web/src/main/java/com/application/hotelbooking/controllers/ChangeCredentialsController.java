package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.ChangeCredentialsDto;
import com.application.hotelbooking.exceptions.CredentialMismatchException;
import com.application.hotelbooking.services.UserService;
import com.application.hotelbooking.services.repositoryservices.UserRepositoryService;
import com.application.hotelbooking.transformers.UserViewTransformer;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping(path = "hotelbooking")
public class ChangeCredentialsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeCredentialsController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private UserViewTransformer userViewTransformer;

    @RequestMapping(value = "/change-password")
    public String changePassword(@Valid @ModelAttribute("credentials") ChangeCredentialsDto changeCredentialsDto, BindingResult result, Authentication auth, HttpSession session, Model model){
        if (result.hasErrors()){
            LOGGER.info("Error while validating");
            return "account";
        }

        try {
            userService.changePassword(auth.getName(),changeCredentialsDto.getNewPassword(), changeCredentialsDto.getOldPassword());
        } catch (OptimisticLockException ole){
            LOGGER.error("OptimisticLockException while changing password.");
            return "redirect:/hotelbooking/account?error";
        } catch (CredentialMismatchException cme){
            LOGGER.error("CredentialMismatchException while changing password.");
            result.rejectValue("oldPassword", "account.form.old.password.not.found");
            return "account";
        }
        LOGGER.info("Successfully changed password!");

        return "redirect:/hotelbooking/account?success";
    }

    @GetMapping("/account")
    public String changeCredentials(HttpSession session, Authentication auth, Model model){
        LOGGER.info("Navigating to account page");
        ChangeCredentialsDto changeCredentialsDto = new ChangeCredentialsDto();
        model.addAttribute("credentials", changeCredentialsDto);
        return "account";
    }
}
