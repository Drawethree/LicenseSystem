package dev.drawethree.LicenseSystem.license.controller;

import dev.drawethree.LicenseSystem.license.model.License;
import dev.drawethree.LicenseSystem.license.service.LicenseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/license")
public class LicenseRestController {

    private final LicenseService licenseService;

    public LicenseRestController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping("/{licenseKey}")
    public License findById(@PathVariable String licenseKey) {
        Optional<License> licenseOptional = licenseService.findByLicenseKey(licenseKey);
        return licenseOptional.orElse(null);
    }

    @GetMapping
    public List<License> getAll() {
        return licenseService.findAll();
    }

}