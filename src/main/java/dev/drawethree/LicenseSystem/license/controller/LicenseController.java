package dev.drawethree.LicenseSystem.license.controller;

import dev.drawethree.LicenseSystem.license.model.License;
import dev.drawethree.LicenseSystem.license.service.LicenseService;
import dev.drawethree.LicenseSystem.license.validation.LicenseValidator;
import dev.drawethree.LicenseSystem.security.service.SecurityService;
import dev.drawethree.LicenseSystem.software.model.Software;
import dev.drawethree.LicenseSystem.software.service.SoftwareService;
import dev.drawethree.LicenseSystem.user.model.User;
import dev.drawethree.LicenseSystem.user.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("license")
public class LicenseController {

    private final LicenseService licenseService;

    private final SoftwareService softwareService;

    private final SecurityService securityService;

    private final LicenseValidator licenseValidator;

    private final UserService userService;

    public LicenseController(LicenseService licenseService, SoftwareService softwareService, SecurityService securityService, LicenseValidator licenseValidator, UserService userService) {
        this.licenseService = licenseService;
        this.softwareService = softwareService;
        this.securityService = securityService;
        this.licenseValidator = licenseValidator;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')")
    public String showIndex(Model model) {

        User user = securityService.getCurrentUser();

        List<License> licenses = licenseService.findAllByLicenseUser(user);

        model.addAttribute("licenses", licenses);

        return "license/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('CREATOR', 'ADMIN')")
    public String showCreateLicenseForm(Model model, RedirectAttributes redirectAttributes) {

        User user = securityService.getCurrentUser();

        License license = new License();
        license.setLicenseKey(licenseService.generateLicenseKey());

        List<Software> softwareList;

        if (user.isAdmin()) {
            softwareList = softwareService.findAll();
        } else {
            softwareList = softwareService.findAllByCreator(user);
        }

        if (softwareList.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "You do not have any software.");
            return "redirect:/software";
        }

        model.addAttribute("license", license);
        model.addAttribute("softwares", softwareList);

        return "license/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('CREATOR', 'ADMIN')")
    public String createLicense(@ModelAttribute("license") License license, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        licenseValidator.validate(license, bindingResult);

        if (bindingResult.hasErrors()) {
            return "license/create";
        }

        User user = securityService.getCurrentUser();

        if (!userService.canCreateLicense(user, license.getSoftware())) {
            redirectAttributes.addFlashAttribute("error", "You have reached maximum amount of licenses for this software!");
            return "redirect:/software";
        }

        license.setCreatedAt(LocalDateTime.now());

        licenseService.save(license);

        redirectAttributes.addFlashAttribute("success", "Successfully created new license for " + license.getSoftware().getName() + ".");

        return "redirect:/software";
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')")
    public String deleteLicense(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {

        User currentUser = securityService.getCurrentUser();

        Optional<License> licenseOptional = licenseService.findById(id);

        if (licenseOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "License not found.");
            return "redirect:/license";
        }

        License license = licenseOptional.get();

        if (!license.getSoftware().getCreator().equals(currentUser) && !currentUser.isAdmin()) {
            return "redirect:/error/error-403";
        }

        licenseService.deleteById(id);

        redirectAttributes.addFlashAttribute("success", "Successfully deleted license.");

        return "redirect:/software";
    }

    @GetMapping("/activate")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')")
    public String showActivationForm(Model model) {

        model.addAttribute("license", new License());

        return "license/activate";
    }

    @PostMapping("/activate")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')")
    public String activateLicense(@ModelAttribute("license") License tempLicense, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

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

        license.activate(user);

        licenseService.save(license);

        redirectAttributes.addFlashAttribute("success", "Successfully activate license for " + license.getSoftware().getName() + ".");

        return "redirect:/license";
    }
}
