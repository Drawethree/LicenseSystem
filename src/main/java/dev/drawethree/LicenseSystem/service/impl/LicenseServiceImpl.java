package dev.drawethree.LicenseSystem.service.impl;

import dev.drawethree.LicenseSystem.model.License;
import dev.drawethree.LicenseSystem.repository.LicenseRepository;
import dev.drawethree.LicenseSystem.service.LicenseService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
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
    public Optional<License> getByLicenseUserAndSoftwareId(String licenseUser, int softwareId) {
        return licenseRepository.findByLicenseUserAndSoftwareId(licenseUser,softwareId);
    }

    @Override
    public Optional<License> getByLicenseKey(String licenseKey) {
        return licenseRepository.findByLicenseKey(licenseKey);
    }

    @Override
    public void save(License license) {
        this.licenseRepository.save(license);
    }

    @Override
    public void delete(License license) {
        this.licenseRepository.delete(license);
    }

    @Override
    public long getLicenseCount() {
        return licenseRepository.count();
    }

    @Override
    public void deleteById(int licenseId) {
        licenseRepository.deleteById(licenseId);
    }
}
