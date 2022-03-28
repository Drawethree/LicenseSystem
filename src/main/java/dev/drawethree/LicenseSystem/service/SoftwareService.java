package dev.drawethree.LicenseSystem.service;

import dev.drawethree.LicenseSystem.model.Software;

import java.util.List;

public interface SoftwareService {

    Software getById(int id);

    List<Software> findAll();

    void save(Software software);

    void delete(Software software);

    void deleteById(int id);
}
