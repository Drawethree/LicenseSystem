package dev.drawethree.LicenseSystem.license.generation.impl;

import dev.drawethree.LicenseSystem.license.generation.LicenseGenerator;
import org.springframework.stereotype.Component;

@Component
public class CryptoLicenseGenerator implements LicenseGenerator {

    @Override
    public String generateLicenseKey() {
        return null;
    }
}
