package dev.drawethree.LicenseSystem.user.controller;

import dev.drawethree.LicenseSystem.security.service.SecurityService;
import dev.drawethree.LicenseSystem.user.model.User;
import dev.drawethree.LicenseSystem.user.service.UserService;
import dev.drawethree.LicenseSystem.user.validation.UserValidator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Optional;


@Controller
@RequestMapping("user")
public class UserController {

    private final SecurityService securityService;


    public UserController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String showUserPanel(Model model) {

        User user = securityService.getCurrentUser();

        model.addAttribute("user", user);

        return "user/index";
    }

}
