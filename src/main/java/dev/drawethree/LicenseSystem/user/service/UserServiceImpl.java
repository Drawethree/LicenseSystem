package dev.drawethree.LicenseSystem.user.service;

import dev.drawethree.LicenseSystem.software.model.Software;
import dev.drawethree.LicenseSystem.user.model.User;
import dev.drawethree.LicenseSystem.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findTopByOrderByCreatedAtDesc() {
        return userRepository.findTopByOrderByCreatedAtDesc();
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public long getUsersCount() {
        return userRepository.count();
    }

    @Override
    @Transactional
    public boolean canCreateSoftware(User user) {
        if (user.isAdmin()) {
            return true;
        }
        return user.isCreator() && user.getSoftwares().size() < User.MAX_SOFTWARE_PER_CREATOR;
    }

    @Override
    @Transactional
    public boolean canCreateLicense(User user, Software software) {
        if (user.isAdmin()) {
            return true;
        }

        return user.isCreator() && software.getCreator().equals(user) && software.getLicenses().size() < User.MAX_LICENSES_PER_SOFTWARE;
    }
}
