package dev.drawethree.LicenseSystem.service.impl;

import dev.drawethree.LicenseSystem.model.License;
import dev.drawethree.LicenseSystem.repository.LicenseRepository;
import dev.drawethree.LicenseSystem.service.LicenseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LicenseServiceImpl implements LicenseService {

    private final LicenseRepository licenseRepository;

    public LicenseServiceImpl(LicenseRepository licenseRepository) {
        this.licenseRepository = licenseRepository;
    }

    @Override
    public License getById(int id) {
        return this.licenseRepository.getById(id);
    }

    @Override
    public List<License> findAll() {
        return this.licenseRepository.findAll();
    }

    @Override
    public List<License> getBySoftwareId(int softwareId) {
        return this.licenseRepository.findAllBySoftwareId(softwareId);
    }

    @Override
    public void save(License license) {
        this.licenseRepository.save(license);
    }

    @Override
    public void delete(License license) {
        this.licenseRepository.delete(license);
    }
}
