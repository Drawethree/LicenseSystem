package dev.drawethree.LicenseSystem.software.repository;


import dev.drawethree.LicenseSystem.software.model.Software;
import dev.drawethree.LicenseSystem.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SoftwareRepository extends JpaRepository<Software, Integer> {

    List<Software> findAllByCreator(User creator);

    List<Software> findAllByVisible(boolean visible);

    Optional<Software> findByName(String name);

}