package dev.drawethree.LicenseSystem.validation;

import dev.drawethree.LicenseSystem.model.License;
import dev.drawethree.LicenseSystem.model.Software;
import dev.drawethree.LicenseSystem.service.LicenseService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LicenseValidator implements Validator {

    private final LicenseService licenseService;

    public LicenseValidator(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Software.class.equals(clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {
        License license = (License) object;


    }
}
