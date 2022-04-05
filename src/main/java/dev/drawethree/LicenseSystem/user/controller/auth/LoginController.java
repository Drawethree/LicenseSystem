package dev.drawethree.LicenseSystem.user.controller.auth;

import dev.drawethree.LicenseSystem.security.service.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private final SecurityService securityService;


    public LoginController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model, String error) {

        if (securityService.isAuthenticated()) {
            model.addAttribute("login",true);
            return "redirect:/";
        }

        if (error != null) {
            model.addAttribute("error", "Your username and password is invalid.");
        }

        return "user/login";
    }
}
