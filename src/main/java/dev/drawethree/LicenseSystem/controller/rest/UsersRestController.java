package dev.drawethree.LicenseSystem.controller.rest;

import dev.drawethree.LicenseSystem.model.User;
import dev.drawethree.LicenseSystem.repository.UserRepository;
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

    @GetMapping(path = "/{id}")
    public User getById(@PathVariable int id) {
        return userRepository.getById(id);
    }

    @GetMapping(path = "/all")
    public List<User> getAll() {
        return userRepository.findAll();
    }
}