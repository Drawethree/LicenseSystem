package dev.drawethree.LicenseSystem.controller;

import dev.drawethree.LicenseSystem.model.License;
import dev.drawethree.LicenseSystem.model.LicenseStatus;
import dev.drawethree.LicenseSystem.service.LicenseService;
import dev.drawethree.LicenseSystem.utils.LicenseKeyGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/license")
public class LicenseController {

    private final LicenseService licenseService;

    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }


    @GetMapping("/new")
    public String createNewLicense(Model model) {
        model.addAttribute("license",new License());
        return "license/create-license";
    }

    @PostMapping("/created")
    public String saveLicense(@ModelAttribute("license") License license) {
        license.setStatus(LicenseStatus.WAITING_FOR_ACTIVATION);
        license.setLicenseKey(LicenseKeyGenerator.generateNewLicenseKey());
        license.setCreatedAt(LocalDateTime.now());
        licenseService.save(license);
        return "redirect:/software/licenses/?softwareId=" + license.getSoftware().getId();
    }


}
