package dev.drawethree.LicenseSystem.license.controller;

import dev.drawethree.LicenseSystem.license.model.License;
import dev.drawethree.LicenseSystem.license.service.LicenseService;
import dev.drawethree.LicenseSystem.license.validation.LicenseValidator;
import dev.drawethree.LicenseSystem.security.service.SecurityService;
import dev.drawethree.LicenseSystem.user.model.User;
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

    public LicenseController(LicenseService licenseService, SecurityService securityService, LicenseValidator licenseValidator) {
        this.licenseService = licenseService;
        this.securityService = securityService;
        this.licenseValidator = licenseValidator;
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

        license.setCreatedAt(LocalDateTime.now());

        licenseService.save(license);

        redirectAttributes.addFlashAttribute("licenseCreated", true);

        return "redirect:/software";
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAnyAuthority('CREATOR','ADMIN')")
    public String deleteLicense(@RequestParam("licenseId") int id, RedirectAttributes redirectAttributes) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        licenseService.deleteById(id);

        redirectAttributes.addFlashAttribute("licenseDeleted", true);


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

        redirectAttributes.addFlashAttribute("licenseActivated",true);

        return "redirect:/license";
    }
}
