package dev.drawethree.LicenseSystem.license.service;

import dev.drawethree.LicenseSystem.license.model.License;
import dev.drawethree.LicenseSystem.user.model.User;
import dev.drawethree.LicenseSystem.license.repository.LicenseRepository;
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
    public List<License> findAll() {
        return this.licenseRepository.findAll();
    }

    @Override
    public List<License> findAllByLicenseUser(User user) {
        return licenseRepository.findAllByLicenseUser(user);
    }

    @Override
    public List<License> findAllBySoftwareId(int softwareId) {
        return licenseRepository.findAllBySoftwareId(softwareId);
    }

    @Override
    public Optional<License> findByLicenseUserAndSoftwareId(String licenseUser, int softwareId) {
        return licenseRepository.findByLicenseUserAndSoftwareId(licenseUser,softwareId);
    }

    @Override
    public Optional<License> findByLicenseKey(String licenseKey) {
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

    @Override
    public Optional<License> findById(int id) {
        return licenseRepository.findById(id);
    }

}
