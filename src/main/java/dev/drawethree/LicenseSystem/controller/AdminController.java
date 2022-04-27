package dev.drawethree.LicenseSystem.controller;

import dev.drawethree.LicenseSystem.security.service.SecurityService;
import dev.drawethree.LicenseSystem.software.model.Software;
import dev.drawethree.LicenseSystem.software.service.SoftwareService;
import dev.drawethree.LicenseSystem.user.model.User;
import dev.drawethree.LicenseSystem.user.service.UserService;
import dev.drawethree.LicenseSystem.user.validation.UserValidator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin")
public class AdminController {

	private final SecurityService securityService;
	private final UserService userService;
	private final SoftwareService softwareService;
	private final UserValidator userValidator;

	public AdminController(UserService userService, SecurityService securityService, SoftwareService softwareService, UserValidator userValidator) {
		this.securityService = securityService;
		this.userService = userService;
		this.softwareService = softwareService;
		this.userValidator = userValidator;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public String showIndex(Model model) {

		model.addAttribute("user", securityService.getCurrentUser());

		return "admin/index";
	}

	@GetMapping("/users")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String showManageUsersPanel(Model model) {

		List<User> users = userService.findAll();

		model.addAttribute("users", users);

		return "admin/users";
	}

	@GetMapping("/software")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String showManageSoftwarePanel(Model model) {

		Collection<Software> softwares = softwareService.findAll();
		model.addAttribute("softwares", softwares);

		return "admin/software";
	}


	@GetMapping("/users/delete")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String deleteUser(@RequestParam("userId") int id, RedirectAttributes redirectAttributes) {

		Optional<User> optionalUser = userService.findById(id);

		if (optionalUser.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "User not found.");
			return "redirect:/admin/users";
		}

		User user = optionalUser.get();

		if (user.isAdmin()) {
			redirectAttributes.addFlashAttribute("error", "Cannot delete admins.");
			return "redirect:/admin/users";
		}

		userService.deleteById(id);
		redirectAttributes.addFlashAttribute("success", "Successfully deleted user " + user.getUsername() + ".");

		return "redirect:/admin/users";
	}

	@GetMapping("/users/changePassword")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String showChangeUserPassword(@RequestParam("userId") int id, Model model) {

		Optional<User> optionalUser = userService.findById(id);

		if (optionalUser.isEmpty()) {
			return "redirect:/admin/users";
		}

		model.addAttribute("user", optionalUser.get());

		return "admin/change_password";
	}

	@PostMapping("/users/changePassword")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String changeUserPassword(@RequestParam("userId") int userId, @ModelAttribute("user") User userView, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

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

		redirectAttributes.addFlashAttribute("success", "Successfully changed password for " + user.getUsername() + ".");

		return "redirect:/admin/users";
	}

	@GetMapping("/users/create")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String showCreateNewUserModel(Model model) {

		model.addAttribute("user", new User());

		return "admin/create_user";
	}

	@PostMapping("/users/create")
	public String createUser(@ModelAttribute("user") User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		userValidator.validate(user, bindingResult);

		if (bindingResult.hasErrors()) {
			return "admin/create_user";
		}

		user.setCreatedAt(LocalDateTime.now());

		userService.saveAndEncryptPassword(user);

		redirectAttributes.addFlashAttribute("success", "Successfully created user " + user.getUsername());

		return "redirect:/admin/users";
	}
}
