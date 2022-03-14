package dev.drawethree.LicenseSystem.controller;

import dev.drawethree.LicenseSystem.model.Software;
import dev.drawethree.LicenseSystem.repo.SoftwareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/software") // This means URL's start with /demo (after Application path)
public class SoftwareController {

	@Autowired
	private SoftwareRepository softwareRepository;

	@PostMapping(path = "/new") // Map ONLY POST Requests
	public @ResponseBody
	Software createNewSoftware(@RequestParam String name, String description) {
		Software sw = new Software();

		sw.setName(name);
		sw.setDescription(description);

		softwareRepository.save(sw);
		return sw;
	}

	@GetMapping(path = "/{id}")
	public @ResponseBody
	Software getById(@RequestParam Integer id) {
		return softwareRepository.getById(id);
	}

	@GetMapping(path = "/all")
	public @ResponseBody
	List<Software> getAllSoftwares() {
		return softwareRepository.findAll();
	}
}