package dev.drawethree.LicenseSystem.controller;

import dev.drawethree.LicenseSystem.model.Software;
import dev.drawethree.LicenseSystem.model.User;
import dev.drawethree.LicenseSystem.service.SecurityService;
import dev.drawethree.LicenseSystem.service.SoftwareService;
import dev.drawethree.LicenseSystem.validation.SoftwareValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("software")
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
    public String viewSoftwares(Model model) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        User user = securityService.getCurrentUser();

        List<Software> softwareList = this.softwareService.findAllByCreator(user);

        model.addAttribute("softwares", softwareList);

        return "software/list";
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

        Software software = softwareService.getById(id);

        model.addAttribute("software", software);

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
        return "redirect:/software";
    }

    @GetMapping("/delete")
    public String deleteSoftware(@RequestParam("softwareId") int id) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        softwareService.deleteById(id);
        return "redirect:/software/";
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
        return "redirect:/software";
    }
}
