package dev.drawethree.LicenseSystem.service;

import dev.drawethree.LicenseSystem.model.License;

import java.util.List;

public interface LicenseService {

    License getById(int id);

    List<License> findAll();

    List<License> getBySoftwareId(int softwareId);

    void save(License license);

    void delete(License license);

}
