package dev.drawethree.LicenseSystem.repository;


import dev.drawethree.LicenseSystem.model.Software;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoftwareRepository extends JpaRepository<Software, Integer> {

}