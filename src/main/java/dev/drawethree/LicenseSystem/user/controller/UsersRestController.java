package dev.drawethree.LicenseSystem.user.controller;

import dev.drawethree.LicenseSystem.exception.UserNotFoundException;
import dev.drawethree.LicenseSystem.software.model.Software;
import dev.drawethree.LicenseSystem.software.model.SoftwareDTO;
import dev.drawethree.LicenseSystem.user.model.User;
import dev.drawethree.LicenseSystem.user.model.UserDTO;
import dev.drawethree.LicenseSystem.user.repository.UserRepository;
import dev.drawethree.LicenseSystem.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/users")
public class UsersRestController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    public UsersRestController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable int id) {
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with id=" + id + " not found.");
        }

        UserDTO userDTO = this.convertToDto(userOptional.get());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}/software")
    public ResponseEntity<List<SoftwareDTO>> getUserSoftware(@PathVariable int id) {
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with id=" + id + " not found.");
        }

        User user = userOptional.get();

        return new ResponseEntity<>(user.getSoftwares().stream().map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}/licenses")
    public ResponseEntity<List<SoftwareDTO>> getUserLicenses(@PathVariable int id) {
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with id=" + id + " not found.");
        }

        User user = userOptional.get();

        return new ResponseEntity<>(user.getSoftwares().stream().map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        List<UserDTO> usersDTO = userService.findAll().stream().filter(user -> !user.isAdmin()).map(this::convertToDto).collect(Collectors.toList());
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }

    private UserDTO convertToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private SoftwareDTO convertToDto(Software software) {
        return modelMapper.map(software, SoftwareDTO.class);
    }
}