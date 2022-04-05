package dev.drawethree.LicenseSystem.controller;

import dev.drawethree.LicenseSystem.model.License;
import dev.drawethree.LicenseSystem.model.LicenseStatus;
import dev.drawethree.LicenseSystem.model.Software;
import dev.drawethree.LicenseSystem.model.User;
import dev.drawethree.LicenseSystem.service.LicenseService;
import dev.drawethree.LicenseSystem.service.SecurityService;
import dev.drawethree.LicenseSystem.service.SoftwareService;
import dev.drawethree.LicenseSystem.utils.LicenseKeyGenerator;
import dev.drawethree.LicenseSystem.validation.LicenseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("license")
public class LicenseController {

    private final LicenseService licenseService;

    private final SoftwareService softwareService;

    private final SecurityService securityService;

    private final LicenseValidator licenseValidator;

    public LicenseController(LicenseService licenseService, SoftwareService softwareService, SecurityService securityService, LicenseValidator licenseValidator) {
        this.licenseService = licenseService;
        this.softwareService = softwareService;
        this.securityService = securityService;
        this.licenseValidator = licenseValidator;
    }

    @GetMapping
    public String viewLicenses(Model model) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        User user = securityService.getCurrentUser();


        List<Software> softwareList = softwareService.findAllByCreator(user);

        List<License> licenses = new ArrayList<>();

        softwareList.forEach(software -> licenses.addAll(software.getLicenses()));

        model.addAttribute("licenses", licenses);

        return "license/list";
    }

    @GetMapping("/create")
    public String showCreateLicenseForm(Model model) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        User user = securityService.getCurrentUser();

        License license = new License();
        license.setLicenseKey(LicenseKeyGenerator.generateNewLicenseKey());

        model.addAttribute("license", license);
        model.addAttribute("softwares", user.getSoftwares());
        return "license/create";
    }

    @PostMapping("/create")
    public String createLicense(@ModelAttribute("license") License license, BindingResult bindingResult) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        licenseValidator.validate(license, bindingResult);

        if (bindingResult.hasErrors()) {
            return "license/create";
        }

        license.setStatus(LicenseStatus.WAITING_FOR_ACTIVATION);
        license.setCreatedAt(LocalDateTime.now());

        licenseService.save(license);
        return "redirect:/license";
    }

    @GetMapping("/delete")
    public String deleteLicense(@RequestParam("licenseId") int id) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        licenseService.deleteById(id);
        return "redirect:/license";
    }
}
