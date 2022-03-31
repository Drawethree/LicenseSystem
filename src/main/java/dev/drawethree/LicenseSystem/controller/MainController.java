package dev.drawethree.LicenseSystem.controller;

import dev.drawethree.LicenseSystem.LicenseSystemApplication;
import dev.drawethree.LicenseSystem.model.User;
import dev.drawethree.LicenseSystem.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class MainController {

    private final UserRepository userRepository;

    public MainController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String showMainPage(Model model) {
        return "index";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "user/login";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        model.addAttribute("logout", true);
        return "index";
    }

    @PostMapping("/process_register")
    public String processRegister(Model model, User user) {

        Optional<User> byName = userRepository.findByUserName(user.getUserName());

        if (byName.isPresent()) {
            model.addAttribute("user", user);
            model.addAttribute("register_failed_message", "User with such username already exists!");
            return "user/register";
        }

        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());

        if (byEmail.isPresent()) {
            model.addAttribute("user", user);
            model.addAttribute("register_failed_message", "User with such email already exists!");
            return "user/register";
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        model.addAttribute("register_success", true);
        return "index";
    }

    @PostMapping("/process_login")
    public String processLogin(Model model, User user) {

        Optional<User> optionalUser = userRepository.findByUserName(user.getUserName());

        if (optionalUser.isEmpty()) {
            optionalUser = userRepository.findByEmail(user.getUserName());
        }

        if (optionalUser.isEmpty()) {
            model.addAttribute("login_failed_message", "User with such credentials does not exists!");
            return "user/login";
        }

        User fromDb = optionalUser.get();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(user.getPassword(), fromDb.getPassword())) {
            model.addAttribute("login_failed_message", "Invalid password!");
            return "user/login";
        }

        model.addAttribute("login_success", true);
        //TODO: Add redirect to main panel
        return "index";
    }

}
