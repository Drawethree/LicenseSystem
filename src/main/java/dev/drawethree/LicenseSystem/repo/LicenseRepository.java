package dev.drawethree.LicenseSystem.repo;

import dev.drawethree.LicenseSystem.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LicenseRepository extends JpaRepository<License, Integer> {
}