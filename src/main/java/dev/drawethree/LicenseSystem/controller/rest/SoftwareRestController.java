package dev.drawethree.LicenseSystem.controller.rest;

import dev.drawethree.LicenseSystem.model.Software;
import dev.drawethree.LicenseSystem.service.SoftwareService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/software")
public class SoftwareRestController {

	private final SoftwareService softwareService;

	public SoftwareRestController(SoftwareService softwareService) {
		this.softwareService = softwareService;
	}

	@GetMapping("/{id}")
	public Software getById(@PathVariable int id) {
		Optional<Software> softwareOptional = softwareService.findById(id);
		return softwareOptional.orElse(null);
	}

	@GetMapping
	public List<Software> getAll() {
		return softwareService.findAll();
	}
}