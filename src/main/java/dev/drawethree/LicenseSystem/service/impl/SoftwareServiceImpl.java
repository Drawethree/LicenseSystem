package dev.drawethree.LicenseSystem.service.impl;

import dev.drawethree.LicenseSystem.model.Software;
import dev.drawethree.LicenseSystem.repository.SoftwareRepository;
import dev.drawethree.LicenseSystem.service.SoftwareService;
import org.springframework.stereotype.Service;

import java.util.List;

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
