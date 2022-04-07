package dev.drawethree.LicenseSystem.license.service;

import dev.drawethree.LicenseSystem.license.model.License;
import dev.drawethree.LicenseSystem.user.model.User;

import java.util.List;
import java.util.Optional;

public interface LicenseService {

    List<License> findAll();

    List<License> findAllByLicenseUser(User user);

    List<License> findAllBySoftwareId(int softwareId);

    Optional<License> findByLicenseUserAndSoftwareId(String licenseUser, int softwareId);

    Optional<License> findByLicenseKey(String licenseKey);

    void save(License license);

    void delete(License license);

    long getLicenseCount();

    void deleteById(int licenseId);

   Optional<License> findById(int id);

   String generateLicenseKey();

}
