package dev.drawethree.LicenseSystem.user.service;

import dev.drawethree.LicenseSystem.software.model.Software;
import dev.drawethree.LicenseSystem.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void save(User user);

    void saveAndEncryptPassword(User user);

    void deleteById(int id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findById(int id);

    Optional<User> findTopByOrderByCreatedAtDesc();

    List<User> findAll();

    long getUsersCount();

    boolean canCreateSoftware(User user);

    boolean canCreateLicense(User user, Software software);
}
