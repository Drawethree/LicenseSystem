package dev.drawethree.LicenseSystem.repository;


import dev.drawethree.LicenseSystem.model.License;
import dev.drawethree.LicenseSystem.model.Software;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface LicenseRepository extends JpaRepository<License, Integer> {

    List<License> findAllBySoftwareId(int softwareId);

    Optional<License> findByLicenseUserAndSoftwareId(String licenseUser, int softwareId);

    Optional<License>  findByLicenseKey(String licenseKey);
}