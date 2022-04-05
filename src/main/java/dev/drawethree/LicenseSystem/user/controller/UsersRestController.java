package dev.drawethree.LicenseSystem.user.controller;

import dev.drawethree.LicenseSystem.user.model.User;
import dev.drawethree.LicenseSystem.user.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users")
public class UsersRestController {

    private final UserRepository userRepository;

    public UsersRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        return userRepository.getById(id);
    }

    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }
}