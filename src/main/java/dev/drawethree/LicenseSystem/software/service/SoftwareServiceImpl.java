package dev.drawethree.LicenseSystem.software.service;

import dev.drawethree.LicenseSystem.software.repository.SoftwareRepository;
import dev.drawethree.LicenseSystem.software.model.Software;
import dev.drawethree.LicenseSystem.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SoftwareServiceImpl implements SoftwareService {

    private final SoftwareRepository softwareRepository;

    public SoftwareServiceImpl(SoftwareRepository softwareRepository) {
        this.softwareRepository = softwareRepository;
    }

    @Override
    public Optional<Software> findById(int id) {
        return this.softwareRepository.findById(id);
    }

    @Override
    public List<Software> findAll() {
        return this.softwareRepository.findAll();
    }

    @Override
    public Page<Software> findPaginated(Pageable pageable, boolean onlyVisible) {

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Software> allSoftwares = onlyVisible ? findAllByVisible(true) : findAll();

        List<Software> returnList;

        if (allSoftwares.size() < startItem) {
            returnList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allSoftwares.size());
            returnList = allSoftwares.subList(startItem, toIndex);
        }

        return new PageImpl<>(returnList, PageRequest.of(currentPage, pageSize), allSoftwares.size());
    }

    @Override
    public List<Software> findAllByVisible(boolean visible) {
        return this.softwareRepository.findAllByVisible(visible);
    }

    @Override
    public List<Software> findAllByCreator(User user) {
        return softwareRepository.findAllByCreator(user);
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
    public void deleteSoftwareById(int id) {
       this.softwareRepository.deleteById(id);
    }

    @Override
    public void updateSoftware(Software software) {
        this.softwareRepository.save(software);
    }

    @Override
    public long getSoftwareCount() {
        return this.softwareRepository.count();
    }
}
