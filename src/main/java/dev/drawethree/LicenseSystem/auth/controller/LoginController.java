package dev.drawethree.LicenseSystem.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("login")
public class LoginController {

    @GetMapping
    public String showLoginPage(Model model, String error) {

        if (error != null) {
            model.addAttribute("error", "Your username or password is invalid.");
        }

        return "auth/login";
    }
}
