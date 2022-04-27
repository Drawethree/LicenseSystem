package dev.drawethree.LicenseSystem.software.controller;

import dev.drawethree.LicenseSystem.exception.SoftwareNotFoundException;
import dev.drawethree.LicenseSystem.software.model.Software;
import dev.drawethree.LicenseSystem.dto.SoftwareDTO;
import dev.drawethree.LicenseSystem.software.service.SoftwareService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/software")
public class SoftwareRestController {


    private final ModelMapper modelMapper;

    private final SoftwareService softwareService;

    public SoftwareRestController(ModelMapper modelMapper, SoftwareService softwareService) {
        this.modelMapper = modelMapper;
        this.softwareService = softwareService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SoftwareDTO> getById(@PathVariable int id) throws SoftwareNotFoundException {
        Optional<Software> softwareOptional = softwareService.findById(id);

        if (softwareOptional.isEmpty()) {
            throw new SoftwareNotFoundException("Software with id=" + id + " not found.");
        }

        Software software = softwareOptional.get();

        return new ResponseEntity<>(this.convertToDto(software), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SoftwareDTO>> getAll() {
        return new ResponseEntity<>(softwareService.findAllByVisible(true).stream().map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    private SoftwareDTO convertToDto(Software software) {
        return modelMapper.map(software, SoftwareDTO.class);
    }
}