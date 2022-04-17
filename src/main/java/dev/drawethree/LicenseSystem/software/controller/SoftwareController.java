package dev.drawethree.LicenseSystem.software.controller;

import dev.drawethree.LicenseSystem.security.service.SecurityService;
import dev.drawethree.LicenseSystem.software.model.Software;
import dev.drawethree.LicenseSystem.software.service.SoftwareService;
import dev.drawethree.LicenseSystem.software.validation.SoftwareValidator;
import dev.drawethree.LicenseSystem.user.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("user/software")
public class SoftwareController {

    private final SoftwareService softwareService;

    private final SecurityService securityService;

    private final SoftwareValidator softwareValidator;

    public SoftwareController(SoftwareService softwareService, SecurityService securityService, SoftwareValidator softwareValidator) {
        this.softwareService = softwareService;
        this.securityService = securityService;
        this.softwareValidator = softwareValidator;
    }

    @GetMapping
    public String index(Model model) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        User user = securityService.getCurrentUser();

        List<Software> softwareList = this.softwareService.findAllByCreator(user);

        model.addAttribute("softwares", softwareList);

        return "software/index";
    }

    @GetMapping("/create")
    public String showCreateNewSoftwareForm(Model model) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        model.addAttribute("software", new Software());
        return "software/create";
    }

    @GetMapping("/update")
    public String showUpdateSoftwareForm(@RequestParam("softwareId") int id, Model model) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        Optional<Software> software = softwareService.findById(id);

        if  (software.isEmpty()) {
            return "software/index";
        }


        model.addAttribute("software", software.get());

        return "software/update";
    }

    @PostMapping("/update")
    public String updateSoftware(@ModelAttribute("software") Software software, BindingResult bindingResult) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        softwareValidator.validate(software, bindingResult);

        if (bindingResult.hasErrors()) {
            return "software/update";
        }

        softwareService.save(software);
        return "redirect:/user/software";
    }

    @GetMapping("/delete")
    public String deleteSoftware(@RequestParam("softwareId") int id) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        softwareService.deleteById(id);
        return "redirect:/user/software";
    }

    @PostMapping("/create")
    public String createSoftware(@ModelAttribute("software") Software software, BindingResult bindingResult) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        softwareValidator.validate(software, bindingResult);

        if (bindingResult.hasErrors()) {
            return "software/create";
        }

        User user = securityService.getCurrentUser();

        software.setCreator(user);
        software.setCreatedAt(LocalDateTime.now());

        softwareService.save(software);
        return "redirect:/user/software";
    }
}
