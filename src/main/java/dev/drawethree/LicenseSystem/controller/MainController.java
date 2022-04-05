package dev.drawethree.LicenseSystem.controller;

import dev.drawethree.LicenseSystem.model.Software;
import dev.drawethree.LicenseSystem.service.LicenseService;
import dev.drawethree.LicenseSystem.service.SecurityService;
import dev.drawethree.LicenseSystem.service.SoftwareService;
import dev.drawethree.LicenseSystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MainController {

    private final UserService userService;

    private final LicenseService licenseService;

    private final SoftwareService softwareService;

    private final SecurityService securityService;

    public MainController(UserService userService, LicenseService licenseService, SoftwareService softwareService, SecurityService securityService) {
        this.userService = userService;
        this.licenseService = licenseService;
        this.softwareService = softwareService;
        this.securityService = securityService;
    }

    @GetMapping("/")
    public String showMainPage(Model model) {

        if (securityService.isAuthenticated()) {
            return "redirect:/panel";
        }


        model.addAttribute("users_count", userService.getUsersCount());
        model.addAttribute("license_count", licenseService.getLicenseCount());
        model.addAttribute("software_count", softwareService.getSoftwareCount());
        model.addAttribute("softwares", softwareService.findAllByVisible(true));

        userService.findTopByOrderByCreatedAtDesc().ifPresent(user -> {
            model.addAttribute("latest_user", user);
        });

        return "index";
    }

}
