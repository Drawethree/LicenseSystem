package dev.drawethree.LicenseSystem.service;

import dev.drawethree.LicenseSystem.model.License;

import java.util.List;
import java.util.Optional;

public interface LicenseService {

    License getById(int id);

    List<License> findAll();

    List<License> getBySoftwareId(int softwareId);

    Optional<License> getByLicenseUserAndSoftwareId(String licenseUser, int softwareId);

    Optional<License> getByLicenseKey(String licenseKey);

    void save(License license);

    void delete(License license);

    long getLicenseCount();

    void deleteById(int licenseId);
}
