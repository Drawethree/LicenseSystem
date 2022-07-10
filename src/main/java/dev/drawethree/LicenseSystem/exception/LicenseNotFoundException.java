package dev.drawethree.LicenseSystem.exception;

import org.springframework.http.HttpStatus;

public class LicenseNotFoundException extends ApiRequestException {

    public LicenseNotFoundException(String errorMessage) {
        super(errorMessage, HttpStatus.NOT_FOUND);
    }
}
