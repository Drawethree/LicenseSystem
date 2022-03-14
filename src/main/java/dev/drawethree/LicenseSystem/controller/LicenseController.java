package dev.drawethree.LicenseSystem.controller;

import dev.drawethree.LicenseSystem.model.License;
import dev.drawethree.LicenseSystem.model.Software;
import dev.drawethree.LicenseSystem.repo.LicenseRepository;
import dev.drawethree.LicenseSystem.service.LicenseKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/api/license") // This means URL's start with /demo (after Application path)
public class LicenseController {

	@Autowired
	private LicenseRepository licenseRepository;

	@PostMapping(path = "/new") // Map ONLY POST Requests
	public @ResponseBody
	License createNewLicense(@RequestParam String userName
			, @RequestParam Software software) {
		License license = new License();

		license.setCreatedAt(LocalDateTime.now());
		license.setLicenseUser(userName);
		license.setLicenseKey(LicenseKeyGenerator.generateNewLicenseKey());
		license.setSoftware(software);

		licenseRepository.save(license);
		return license;
	}

	@GetMapping(path = "/{id}")
	public @ResponseBody
	License getById(@RequestParam Integer id) {
		return licenseRepository.getById(id);
	}

	@GetMapping(path = "/all")
	public @ResponseBody
	List<License> getAllLicense() {
		return licenseRepository.findAll();
	}
}