package dev.drawethree.LicenseSystem.service;

import dev.drawethree.LicenseSystem.model.Software;
import dev.drawethree.LicenseSystem.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    void save(User user);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findTopByOrderByCreatedAtDesc();

    Collection<User> findAll();

    long getUsersCount();

    boolean canCreateSoftware(User user);

    boolean canCreateLicense(User user, Software software);
}
