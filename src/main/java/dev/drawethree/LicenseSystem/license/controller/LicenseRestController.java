package dev.drawethree.LicenseSystem.license.controller;

import dev.drawethree.LicenseSystem.exception.LicenseAlreadyActivatedException;
import dev.drawethree.LicenseSystem.exception.LicenseNotFoundException;
import dev.drawethree.LicenseSystem.exception.NotAuthorizedException;
import dev.drawethree.LicenseSystem.license.model.License;
import dev.drawethree.LicenseSystem.license.model.LicenseDTO;
import dev.drawethree.LicenseSystem.license.service.LicenseService;
import dev.drawethree.LicenseSystem.security.service.SecurityService;
import dev.drawethree.LicenseSystem.user.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/license")
public class LicenseRestController {

    private final LicenseService licenseService;

    private final SecurityService securityService;

    private final ModelMapper modelMapper;

    public LicenseRestController(LicenseService licenseService, SecurityService securityService, ModelMapper modelMapper) {
        this.licenseService = licenseService;
        this.securityService = securityService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<LicenseDTO>> findAll() {
        User authUser = securityService.getCurrentUser();

        List<LicenseDTO> licenseDTOS = new ArrayList<>();

        if (authUser.isAdmin()) {
            licenseDTOS = licenseService.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
        } else if (authUser.isCustomer()) {
            licenseDTOS = licenseService.findAllByLicenseUser(authUser).stream().map(this::convertToDto).collect(Collectors.toList());
        }

        return new ResponseEntity<>(licenseDTOS,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LicenseDTO> findById(@PathVariable("id") int id) {

        Optional<License> licenseOptional = licenseService.findById(id);

        if (licenseOptional.isEmpty()) {
            throw new LicenseNotFoundException("License with id=" + id + " not found.");
        }

        License license = licenseOptional.get();
        User user = securityService.getCurrentUser();
        User licenseUser = license.getLicenseUser();

        if ((licenseUser != null && !user.isAdmin() && !user.equals(licenseUser)) || (!user.equals(license.getSoftware().getCreator()) && !user.isAdmin())) {
            throw new NotAuthorizedException("Not authorized.");
        }

        return new ResponseEntity<>(convertToDto(license), HttpStatus.OK);
    }

    /*@PostMapping("/activate")
    public ResponseEntity<LicenseDTO> activateLicense(@RequestParam("licenseKey") String licenseKey) {

        Optional<License> licenseOptional = licenseService.findByLicenseKey(licenseKey);

        if (licenseOptional.isEmpty()) {
            throw new LicenseNotFoundException("License with key=" + licenseKey + " not found.");
        }

        License license = licenseOptional.get();

        if (license.getLicenseUser() != null) {
            throw new LicenseAlreadyActivatedException("License is already activated.");
        }

        User authUser = securityService.getCurrentUser();

        license.setLicenseUser(authUser);
        licenseService.save(license);

        return new ResponseEntity<>(convertToDto(license), HttpStatus.CREATED);
    }*/

    private LicenseDTO convertToDto(License license) {
        return modelMapper.map(license, LicenseDTO.class);
    }

}