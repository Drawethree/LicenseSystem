package dev.drawethree.LicenseSystem.exception;


import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiRequestException {

    public UserNotFoundException(String errorMessage) {
        super(errorMessage, HttpStatus.NOT_FOUND);
    }
}
