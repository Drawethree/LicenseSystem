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

    private final SecurityService securityService;

    private final LicenseValidator licenseValidator;

    private final UserService userService;

    public LicenseController(LicenseService licenseService, SecurityService securityService, LicenseValidator licenseValidator, SoftwareService softwareService, UserService userService) {
        this.licenseService = licenseService;
        this.securityService = securityService;
        this.licenseValidator = licenseValidator;
        this.userService = userService;
    }

    @GetMapping
    public String showIndex(Model model) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        User user = securityService.getCurrentUser();

        List<License> licenses = licenseService.findAllByLicenseUser(user);

        model.addAttribute("licenses", licenses);

        return "license/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('CREATOR','ADMIN')")
    public String showCreateLicenseForm(Model model) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        User user = securityService.getCurrentUser();

        License license = new License();
        license.setLicenseKey(licenseService.generateLicenseKey());

        model.addAttribute("license", license);
        model.addAttribute("softwares", user.getSoftwares());
        return "license/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('CREATOR','ADMIN')")
    public String createLicense(@ModelAttribute("license") License license, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        licenseValidator.validate(license, bindingResult);

        if (bindingResult.hasErrors()) {
            return "license/create";
        }

        User user = securityService.getCurrentUser();

        if (!userService.canCreateLicense(user,license.getSoftware())) {
            redirectAttributes.addFlashAttribute("error", "You have reached maximum amount of licenses for this software!");
            return "redirect:/software";
        }

        license.setCreatedAt(LocalDateTime.now());

        licenseService.save(license);

        redirectAttributes.addFlashAttribute("success", "Successfully created new license!");

        return "redirect:/software";
    }

    @GetMapping("/delete/{id}")
    public String deleteLicense(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        User currentUser = securityService.getCurrentUser();

        Optional<License> licenseOptional = licenseService.findById(id);

        if (licenseOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "License does not exist.");
            return "redirect:/license";
        }

        if (!licenseOptional.get().getSoftware().getCreator().equals(currentUser) && !currentUser.isAdmin()) {
            redirectAttributes.addFlashAttribute("error", "Not authorized.");
            return "redirect:/license";
        }

        licenseService.deleteById(id);

        redirectAttributes.addFlashAttribute("success", "Successfully deleted license!");

        return "redirect:/software";
    }

    @GetMapping("/activate")
    public String showActivationForm(Model model) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        model.addAttribute("license", new License());

        return "license/activate";
    }

    @PostMapping("/activate")
    public String activateLicense(@ModelAttribute("license") License tempLicense, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

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

        redirectAttributes.addFlashAttribute("success","Successfully activate license!");

        return "redirect:/license";
    }
}
