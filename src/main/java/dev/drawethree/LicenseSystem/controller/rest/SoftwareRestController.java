package dev.drawethree.LicenseSystem.controller.rest;

import dev.drawethree.LicenseSystem.model.Software;
import dev.drawethree.LicenseSystem.service.impl.SoftwareServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/software")
public class SoftwareRestController {

	private final SoftwareServiceImpl softwareService;

	public SoftwareRestController(SoftwareServiceImpl softwareService) {
		this.softwareService = softwareService;
	}

	@GetMapping(path = "/{id}")
	public Software getById(@PathVariable int id) {
		return softwareService.getById(id);
	}

	@GetMapping(path = "/all")
	public List<Software> getAll() {
		return softwareService.findAll();
	}
}