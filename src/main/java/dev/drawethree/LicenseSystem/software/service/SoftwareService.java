package dev.drawethree.LicenseSystem.software.service;

import dev.drawethree.LicenseSystem.software.model.Software;
import dev.drawethree.LicenseSystem.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SoftwareService {

    Optional<Software> findById(int id);

    List<Software> findAll();

    Page<Software> findPaginated(Pageable pageable, boolean onlyVisible);

    List<Software> findAllByVisible(boolean visibility);

    List<Software> findAllByCreator(User user);

    Optional<Software> findByName(String name);

    void save(Software software);

    void delete(Software software);

    void deleteById(int id);

    long getSoftwareCount();
}
