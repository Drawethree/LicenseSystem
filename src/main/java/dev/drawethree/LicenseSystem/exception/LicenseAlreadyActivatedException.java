package dev.drawethree.LicenseSystem.exception;

import org.springframework.http.HttpStatus;

public class LicenseAlreadyActivatedException extends ApiRequestException {
    public LicenseAlreadyActivatedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
