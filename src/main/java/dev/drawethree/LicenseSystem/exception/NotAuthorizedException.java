package dev.drawethree.LicenseSystem.exception;

import org.springframework.http.HttpStatus;

public class NotAuthorizedException extends ApiRequestException {

    public NotAuthorizedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
