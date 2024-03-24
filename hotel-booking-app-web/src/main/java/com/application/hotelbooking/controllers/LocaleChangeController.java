package com.application.hotelbooking.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

@Controller
@RequestMapping(path = "hotelbooking")
public class LocaleChangeController {

    @GetMapping("/changeLanguage")
    public String changeLanguage(@RequestParam String lang, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.setAttribute("lang", lang); // Store language in session

        LocaleContextHolder.setLocale(new Locale(lang));

        return "redirect:" + request.getHeader("Referer");
//        return "redirect:" + "/hotelbooking/home";
    }
}
