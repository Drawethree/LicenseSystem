package dev.drawethree.LicenseSystem.license.validation;

import dev.drawethree.LicenseSystem.license.model.License;
import dev.drawethree.LicenseSystem.software.model.Software;
import dev.drawethree.LicenseSystem.license.service.LicenseService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
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

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "software", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "duration", "field.required");

        if (license.getDuration() < 0) {
            errors.rejectValue("duration", "license.duration.length");
        }
    }
}
