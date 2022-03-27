package dev.drawethree.licensesystem.controller;

import dev.drawethree.licensesystem.model.License;
import dev.drawethree.licensesystem.model.LicenseStatus;
import dev.drawethree.licensesystem.model.Software;
import dev.drawethree.licensesystem.service.LicenseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/license")
public class LicenseController {

    private final LicenseService licenseService;

    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }


    @GetMapping("/create")
    public String createNewLicense(@ModelAttribute("software") Software software, Model model) {

        License license = new License();
        license.setSoftware(software);
        license.setStatus(LicenseStatus.WAITING_FOR_ACTIVATION);

        model.addAttribute("software", software);
        model.addAttribute("license", license);

        return "license/create-license";
    }

    @PostMapping("/save")
    public String saveLicense(@ModelAttribute("license") License license) {
        license.setCreatedAt(LocalDateTime.now());
        licenseService.save(license);
        return "redirect:/software/licenses/?softwareId=" + license.getSoftware().getId();
    }


}
