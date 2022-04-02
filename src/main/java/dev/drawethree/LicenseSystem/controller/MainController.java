package dev.drawethree.LicenseSystem.controller;

import dev.drawethree.LicenseSystem.service.LicenseService;
import dev.drawethree.LicenseSystem.service.SoftwareService;
import dev.drawethree.LicenseSystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {


    private final UserService userService;

    private final LicenseService licenseService;

    private final SoftwareService softwareService;

    public MainController(UserService userService, LicenseService licenseService, SoftwareService softwareService) {
        this.userService = userService;
        this.licenseService = licenseService;
        this.softwareService = softwareService;
    }

    @GetMapping("/")
    public String showMainPage(Model model) {
        model.addAttribute("users_count", userService.getUsersCount());
        model.addAttribute("license_count", licenseService.getLicenseCount());
        model.addAttribute("software_count", softwareService.getSoftwareCount());

        userService.findTopByOrderByCreatedAtDesc().ifPresent(user -> {
            model.addAttribute("latest_user",user);
        });
        return "index";
    }

}
