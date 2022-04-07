package dev.drawethree.LicenseSystem.license.generation.impl;

import dev.drawethree.LicenseSystem.license.generation.LicenseGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDLicenseGenerator implements LicenseGenerator {

    @Override
    public String generateLicenseKey() {
        return UUID.randomUUID().toString();
    }
}
