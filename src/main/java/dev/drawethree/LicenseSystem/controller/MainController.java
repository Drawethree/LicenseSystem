package dev.drawethree.LicenseSystem.controller;

import dev.drawethree.LicenseSystem.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {


    @GetMapping("/")
    public String showIndex(Model model) {
        model.addAttribute("user", new User());
        return "index.html";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "user/login";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        model.addAttribute("logout", true);
        return "redirect:/index";
    }

}
