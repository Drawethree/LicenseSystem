package dev.drawethree.LicenseSystem.controller.rest;

import dev.drawethree.LicenseSystem.model.License;
import dev.drawethree.LicenseSystem.service.LicenseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/license")
public class LicenseRestController {

    private final LicenseService licenseService;

    public LicenseRestController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping("/{id}")
    public License findById(@PathVariable int id) {
        Optional<License> licenseOptional = licenseService.findById(id);
        return licenseOptional.orElse(null);
    }

    @GetMapping
    public List<License> getAll() {
        return licenseService.findAll();
    }

}