package dev.drawethree.LicenseSystem.service;

import dev.drawethree.LicenseSystem.model.License;

import java.util.List;
import java.util.Optional;

public interface LicenseService {

    List<License> findAll();

    List<License> getBySoftwareId(int softwareId);

    Optional<License> getByLicenseUserAndSoftwareId(String licenseUser, int softwareId);

    Optional<License> getByLicenseKey(String licenseKey);

    void save(License license);

    void delete(License license);

    long getLicenseCount();

    void deleteById(int licenseId);

   Optional<License> findById(int id);
}
