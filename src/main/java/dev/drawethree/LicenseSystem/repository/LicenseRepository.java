package dev.drawethree.licensesystem.repository;


import dev.drawethree.licensesystem.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LicenseRepository extends JpaRepository<License, Integer> {
    List<License> findAllBySoftwareId(int softwareId);
}