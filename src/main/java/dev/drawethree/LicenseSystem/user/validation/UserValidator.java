package dev.drawethree.LicenseSystem.user.validation;

import dev.drawethree.LicenseSystem.user.model.User;
import dev.drawethree.LicenseSystem.user.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {
        User user = (User) object;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required");


        // Username length check
        if (user.getUsername().length() < 6 || user.getUsername().length() > 32) {
            errors.rejectValue("username", "username.size");
        }

        // Duplicite user check
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            errors.rejectValue("username", "user.username.taken");
        }

        // Duplicite email check
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "user.email.taken");
        }
        // pw length check
        /*if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "password.size");
        }*/

    }
}
