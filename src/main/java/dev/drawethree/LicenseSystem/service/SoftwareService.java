package dev.drawethree.LicenseSystem.service;

import dev.drawethree.LicenseSystem.model.Software;
import dev.drawethree.LicenseSystem.model.User;

import java.util.List;
import java.util.Optional;

public interface SoftwareService {

    Software getById(int id);

    List<Software> findAll();

    List<Software> findAllByUser(User user);

    Optional<Software> findByName(String name);

    void save(Software software);

    void delete(Software software);

    void deleteById(int id);

    long getSoftwareCount();
}
