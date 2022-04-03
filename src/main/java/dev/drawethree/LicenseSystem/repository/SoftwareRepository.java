package dev.drawethree.LicenseSystem.repository;


import dev.drawethree.LicenseSystem.model.Software;
import dev.drawethree.LicenseSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SoftwareRepository extends JpaRepository<Software, Integer> {

    List<Software> findAllByUser(User user);

    Optional<Software> findByName(String name);

}