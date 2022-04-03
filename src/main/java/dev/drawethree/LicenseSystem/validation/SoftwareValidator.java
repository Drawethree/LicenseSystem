package dev.drawethree.LicenseSystem.validation;

import dev.drawethree.LicenseSystem.model.Software;
import dev.drawethree.LicenseSystem.service.SoftwareService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class SoftwareValidator implements Validator {

    private final SoftwareService softwareService;

    public SoftwareValidator(SoftwareService softwareService) {
        this.softwareService = softwareService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Software.class.equals(clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {
        Software software = (Software) object;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "field.required");

        if (softwareService.findByName(software.getName()).isPresent()) {
            errors.rejectValue("name", "software.name.taken");
        }
    }

}
