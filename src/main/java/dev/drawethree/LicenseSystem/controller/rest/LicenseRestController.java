package dev.drawethree.LicenseSystem.controller.rest;

import dev.drawethree.LicenseSystem.model.License;
import dev.drawethree.LicenseSystem.service.impl.LicenseServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/license")
public class LicenseRestController {

    private final LicenseServiceImpl licenseService;

    public LicenseRestController(LicenseServiceImpl licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping("/{id}")
    public License getById(@PathVariable int id) {
        return licenseService.getById(id);
    }

    @GetMapping
    public List<License> getAll() {
        return licenseService.findAll();
    }

}