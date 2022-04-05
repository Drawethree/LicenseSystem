package dev.drawethree.LicenseSystem.user.controller.auth;

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

import java.time.LocalDateTime;

@Controller
public class RegisterController {

    private final UserService userService;

    private final SecurityService securityService;

    private final UserValidator userValidator;

    public RegisterController(UserService userService, SecurityService securityService, UserValidator userValidator) {
        this.userService = userService;
        this.securityService = securityService;
        this.userValidator = userValidator;
    }


    @GetMapping("/register")
    public String showRegisterPage(Model model) {

        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, BindingResult bindingResult) {

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "user/register";
        }

        user.setCreatedAt(LocalDateTime.now());

        String rawPw = user.getPassword();

        userService.save(user);

        securityService.autoLogin(user.getUsername(), rawPw);

        return "redirect:/";
    }
}
