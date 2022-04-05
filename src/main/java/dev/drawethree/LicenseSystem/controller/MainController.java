package dev.drawethree.LicenseSystem.controller;

import dev.drawethree.LicenseSystem.software.model.Software;
import dev.drawethree.LicenseSystem.license.service.LicenseService;
import dev.drawethree.LicenseSystem.security.service.SecurityService;
import dev.drawethree.LicenseSystem.software.service.SoftwareService;
import dev.drawethree.LicenseSystem.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @GetMapping({"/", "home"})
    public String showMainPage(@RequestParam("page") Optional<Integer> page, Model model) {

        if (securityService.isAuthenticated()) {
            return "redirect:/user";
        }

        int currentPage = page.orElse(1);
        int pageSize = 5;

        Page<Software> softwarePage = softwareService.findPaginated(PageRequest.of(currentPage - 1, pageSize), true);

        int totalPages = softwarePage.getTotalPages();

        if (currentPage > totalPages && totalPages > 0) {
            return "redirect:/";
        }

        model.addAttribute("software_page", softwarePage);
        model.addAttribute("users_count", userService.getUsersCount());
        model.addAttribute("license_count", licenseService.getLicenseCount());
        model.addAttribute("software_count", softwareService.getSoftwareCount());


        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("page_numbers", pageNumbers);
        }

        userService.findTopByOrderByCreatedAtDesc().ifPresent(user -> {
            model.addAttribute("latest_user", user);
        });

        return "index";
    }

}
