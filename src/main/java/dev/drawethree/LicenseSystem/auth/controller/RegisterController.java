package dev.drawethree.LicenseSystem.auth.controller;

import dev.drawethree.LicenseSystem.security.service.SecurityService;
import dev.drawethree.LicenseSystem.user.model.User;
import dev.drawethree.LicenseSystem.user.service.UserService;
import dev.drawethree.LicenseSystem.user.validation.UserValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("register")
public class RegisterController {

    private final UserService userService;

    private final SecurityService securityService;

    private final UserValidator userValidator;

    public RegisterController(UserService userService, SecurityService securityService, UserValidator userValidator) {
        this.userService = userService;
        this.securityService = securityService;
        this.userValidator = userValidator;
    }

    @GetMapping
    public String showRegisterPage(Model model) {

        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping
    public String register(@ModelAttribute("user") User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        user.setCreatedAt(LocalDateTime.now());

        String rawPw = user.getPassword();

        userService.saveAndEncryptPassword(user);

        securityService.autoLogin(user.getUsername(), rawPw);

        redirectAttributes.addFlashAttribute("success", "Successfully registered account and auto-logged in!");

        return "redirect:/";
    }
}
