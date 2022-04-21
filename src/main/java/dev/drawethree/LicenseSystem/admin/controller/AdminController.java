package dev.drawethree.LicenseSystem.admin.controller;

import dev.drawethree.LicenseSystem.license.model.License;
import dev.drawethree.LicenseSystem.license.service.LicenseService;
import dev.drawethree.LicenseSystem.security.service.SecurityService;
import dev.drawethree.LicenseSystem.software.model.Software;
import dev.drawethree.LicenseSystem.software.service.SoftwareService;
import dev.drawethree.LicenseSystem.user.model.User;
import dev.drawethree.LicenseSystem.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@Controller
@RequestMapping("admin")
public class AdminController {

	private final SecurityService securityService;
	private final UserService userService;
	private final LicenseService licenseService;
	private final SoftwareService softwareService;

	public AdminController(UserService userService, SecurityService securityService, LicenseService licenseService, SoftwareService softwareService) {
		this.securityService = securityService;
		this.userService = userService;
		this.licenseService = licenseService;
		this.softwareService = softwareService;
	}

	@GetMapping
	public String showIndex(Model model) {

		if (!securityService.isAuthenticated()) {
			return "redirect:/login";
		}

		model.addAttribute("user", securityService.getCurrentUser());

		return "admin/index";
	}

	@GetMapping("/users")
	public String showManageUsersPanel(Model model) {

		if (!securityService.isAuthenticated()) {
			return "redirect:/login";
		}

		Collection<User> users = userService.findAll();
		users.removeIf(User::isAdmin);

		model.addAttribute("users", users);

		return "admin/users";
	}

	@GetMapping("/software")
	public String showManageSoftwarePanel(Model model) {

		if (!securityService.isAuthenticated()) {
			return "redirect:/login";
		}

		Collection<Software> softwares = softwareService.findAll();
		model.addAttribute("softwares", softwares);

		return "admin/software";
	}

	@GetMapping("/license")
	public String showManageLicensesPanel(Model model) {

		if (!securityService.isAuthenticated()) {
			return "redirect:/login";
		}

		Collection<License> licenses = licenseService.findAll();
		model.addAttribute("licenses", licenses);

		return "admin/licenses";
	}

	@GetMapping("/users/delete")
	public String deleteUser(@RequestParam("userId") int id) {

		if (!securityService.isAuthenticated()) {
			return "redirect:/login";
		}

		userService.deleteById(id);
		return "redirect:/admin/users";
	}

	@GetMapping("/users/changePassword")
	public String showChangeUserPassword(Model model, @RequestParam("userId") int id) {

		if (!securityService.isAuthenticated()) {
			return "redirect:/login";
		}

		Optional<User> optionalUser = userService.findById(id);

		if (optionalUser.isEmpty()) {
			return "redirect:/admin/users";
		}

		model.addAttribute("user", optionalUser.get());

		return "admin/change_password";
	}

	@PostMapping("/users/changePassword")
	public String changeUserPassword(@ModelAttribute("user") User userView, BindingResult bindingResult, @RequestParam int userId) {

		if (bindingResult.hasErrors()) {
			return "admin/change_password";
		}

		Optional<User> optionalUser = userService.findById(userId);

		if (optionalUser.isEmpty()) {
			return "redirect:/admin/users";
		}

		User user = optionalUser.get();

		user.setPassword(userView.getPassword());

		userService.saveAndEncryptPassword(user);

		return "redirect:/admin/users";
	}
}
