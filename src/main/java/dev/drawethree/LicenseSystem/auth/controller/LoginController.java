package dev.drawethree.LicenseSystem.auth.controller;

import dev.drawethree.LicenseSystem.security.service.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("login")
public class LoginController {

    private final SecurityService securityService;


    public LoginController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping
    public String showLoginPage(Model model, String error) {

        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        if (error != null) {
            model.addAttribute("error", "Your username or password is invalid.");
        }

        return "auth/login";
    }
}
