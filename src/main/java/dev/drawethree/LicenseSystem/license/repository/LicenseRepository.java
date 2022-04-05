package dev.drawethree.LicenseSystem.license.repository;


import dev.drawethree.LicenseSystem.license.model.License;
import dev.drawethree.LicenseSystem.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LicenseRepository extends JpaRepository<License, Integer> {

    List<License> findAllBySoftwareId(int softwareId);

    List<License> findAllByLicenseUser(User user);

    Optional<License> findByLicenseUserAndSoftwareId(String licenseUser, int softwareId);

    Optional<License> findByLicenseKey(String licenseKey);
}