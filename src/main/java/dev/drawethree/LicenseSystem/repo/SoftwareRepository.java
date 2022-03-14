package dev.drawethree.LicenseSystem.repo;

import dev.drawethree.LicenseSystem.model.Software;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoftwareRepository extends JpaRepository<Software, Integer> {
}