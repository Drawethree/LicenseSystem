package dev.drawethree.LicenseSystem.validation;

import dev.drawethree.LicenseSystem.model.User;
import dev.drawethree.LicenseSystem.service.UserService;
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
        /*if (user.getUsername().length() < 6 || user.getUsername().length() > 32) {
            errors.rejectValue("username", "username.size");
        }*/

        if (userService.findByUsername(user.getUsername()).isPresent()) {
            errors.rejectValue("username", "username.taken");
        }

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "email.taken");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required");
        /*if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "password.size");
        }*/

    }
}
