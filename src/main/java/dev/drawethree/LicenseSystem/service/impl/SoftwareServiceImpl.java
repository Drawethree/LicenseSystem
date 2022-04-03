package dev.drawethree.LicenseSystem.service.impl;

import dev.drawethree.LicenseSystem.model.Software;
import dev.drawethree.LicenseSystem.model.User;
import dev.drawethree.LicenseSystem.repository.SoftwareRepository;
import dev.drawethree.LicenseSystem.service.SoftwareService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SoftwareServiceImpl implements SoftwareService {

    private final SoftwareRepository softwareRepository;

    public SoftwareServiceImpl(SoftwareRepository softwareRepository) {
        this.softwareRepository = softwareRepository;
    }

    @Override
    public Software getById(int id) {
        return this.softwareRepository.getById(id);
    }

    @Override
    public List<Software> findAll() {
        return this.softwareRepository.findAll();
    }

    @Override
    public List<Software> findAllByUser(User user) {
        return softwareRepository.findAllByUser(user);
    }

    @Override
    public Optional<Software> findByName(String name) {
        return softwareRepository.findByName(name);
    }

    @Override
    public void save(Software software) {
        this.softwareRepository.save(software);
    }

    @Override
    public void delete(Software software) {
        this.softwareRepository.delete(software);
    }

    @Override
    public void deleteById(int id) {
       this.softwareRepository.deleteById(id);
    }

    @Override
    public long getSoftwareCount() {
        return this.softwareRepository.count();
    }
}
