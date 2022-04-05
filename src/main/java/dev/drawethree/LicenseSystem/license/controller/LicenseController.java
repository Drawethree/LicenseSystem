package dev.drawethree.LicenseSystem.license.controller;

import dev.drawethree.LicenseSystem.license.model.License;
import dev.drawethree.LicenseSystem.software.model.Software;
import dev.drawethree.LicenseSystem.user.model.User;
import dev.drawethree.LicenseSystem.license.service.LicenseService;
import dev.drawethree.LicenseSystem.security.service.SecurityService;
import dev.drawethree.LicenseSystem.software.service.SoftwareService;
import dev.drawethree.LicenseSystem.license.utils.LicenseKeyGenerator;
import dev.drawethree.LicenseSystem.license.validation.LicenseValidator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        List<License> licenses = new ArrayList<>();

        if (user.isCreator()) {
            List<Software> softwareList = softwareService.findAllByCreator(user);
            for (Software sw : softwareList) {
                licenses.addAll(sw.getLicenses());
            }
        } else if (user.isCustomer()) {
            licenses = licenseService.findAllByLicenseUser(user);
        }

        model.addAttribute("licenses", licenses);

        return "license/list";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('CREATOR','ADMIN')")
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
    @PreAuthorize("hasAnyAuthority('CREATOR','ADMIN')")
    public String createLicense(@ModelAttribute("license") License license, BindingResult bindingResult) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        licenseValidator.validate(license, bindingResult);

        if (bindingResult.hasErrors()) {
            return "license/create";
        }

        license.setCreatedAt(LocalDateTime.now());

        licenseService.save(license);
        return "redirect:/license";
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAnyAuthority('CREATOR','ADMIN')")
    public String deleteLicense(@RequestParam("licenseId") int id) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        licenseService.deleteById(id);
        return "redirect:/license";
    }

    @GetMapping("/activate")
    @PreAuthorize("hasAnyAuthority('CUSTOMER','CREATOR','ADMIN')")
    public String showActivationForm(Model model) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        model.addAttribute("license", new License());

        return "license/activate";
    }

    @PostMapping("/activate")
    @PreAuthorize("hasAnyAuthority('CUSTOMER','CREATOR','ADMIN')")
    public String activateLicense(@ModelAttribute("license") License tempLicense, BindingResult bindingResult) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        User user = securityService.getCurrentUser();

        Optional<License> optionalLicense = licenseService.findByLicenseKey(tempLicense.getLicenseKey());

        if (optionalLicense.isEmpty()) {
            bindingResult.rejectValue("licenseKey", "license.not.found");
            return "license/activate";
        }

        License license = optionalLicense.get();

        if (license.getLicenseUser() != null) {
            bindingResult.rejectValue("licenseKey", "license.already.activated");
            return "license/activate";
        }

        license.setLicenseUser(user);
        license.setActivationDate(LocalDateTime.now());

        if (license.getDuration() > 0) {
            license.setExpireDate(LocalDateTime.now().plusDays(license.getDuration()));
        }

        licenseService.save(license);
        return "index";
    }
}
